package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.manage.mapper.CollectInfoMapper;
import com.lz.manage.model.domain.CollectFolder;
import com.lz.manage.model.domain.CollectInfo;
import com.lz.manage.model.domain.FileInfo;
import com.lz.manage.model.dto.collectInfo.CollectInfoQuery;
import com.lz.manage.model.vo.collectInfo.CollectInfoVo;
import com.lz.manage.service.ICollectFolderService;
import com.lz.manage.service.ICollectInfoService;
import com.lz.manage.service.IFileInfoService;
import com.lz.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收藏记录Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class CollectInfoServiceImpl extends ServiceImpl<CollectInfoMapper, CollectInfo> implements ICollectInfoService {
    private static final Logger log = LoggerFactory.getLogger(CollectInfoServiceImpl.class);

    /**
     * 导入用户数据校验器
     */
    private static Validator validator;

    @Resource
    private CollectInfoMapper collectInfoMapper;

    @Resource
    private ISysUserService sysUserService;


    @Resource
    private IFileInfoService fileInfoService;

    @Resource
    private ICollectFolderService collectFolderInfoService;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码

    /**
     * 查询收藏记录
     *
     * @param id 收藏记录主键
     * @return 收藏记录
     */
    @Override
    public CollectInfo selectCollectInfoById(Long id) {
        return collectInfoMapper.selectCollectInfoById(id);
    }

    /**
     * 查询收藏记录列表
     *
     * @param collectInfo 收藏记录
     * @return 收藏记录
     */
    @Override
    public List<CollectInfo> selectCollectInfoList(CollectInfo collectInfo) {
        List<CollectInfo> collectInfos = collectInfoMapper.selectCollectInfoList(collectInfo);
        for (CollectInfo info : collectInfos) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
        }
        return collectInfos;
    }

    /**
     * 新增收藏记录
     *
     * @param collectInfo 收藏记录
     * @return 结果
     */
    @Override
    public int insertCollectInfo(CollectInfo collectInfo) {
        FileInfo fileInfo = initData(collectInfo);
        //查询是否已经收藏
        Long userId = SecurityUtils.getUserId();
        CollectInfo collectINfoDb = isCollect(collectInfo.getFileId(), userId);
        if (StringUtils.isNotNull(collectINfoDb)) {
            throw new ServiceException("文件已收藏");
        }
        fileInfo.setCollectCount(fileInfo.getCollectCount() + 1);
        fileInfoService.updateFileInfo(fileInfo);
        collectInfo.setUserId(userId);
        collectInfo.setCreateTime(DateUtils.getNowDate());
        return collectInfoMapper.insertCollectInfo(collectInfo);
    }

    @Override
    public CollectInfo isCollect(Long fileId, Long userId) {
        return this.getOne(new LambdaQueryWrapper<CollectInfo>()
                .eq(CollectInfo::getUserId, userId)
                .eq(CollectInfo::getFileId, fileId)
        );
    }

    private FileInfo initData(CollectInfo collectInfo) {
        //首先查询文件是否存在
        FileInfo fileInfo = fileInfoService.selectFileInfoById(collectInfo.getFileId());
        if (StringUtils.isNull(fileInfo)) {
            throw new ServiceException("文件不存在");
        }
        BeanUtils.copyProperties(fileInfo, collectInfo);
        //查询收藏夹是否存在
        CollectFolder collectFolder = collectFolderInfoService.selectCollectFolderById(collectInfo.getFolderId());
        if (StringUtils.isNull(collectFolder)) {
            throw new ServiceException("收藏夹不存在");
        }
        if (!collectInfo.getUserId().equals(collectFolder.getUserId())) {
            throw new ServiceException("用户无权限操作此收藏夹");
        }
        collectInfo.setFolderName(collectFolder.getName());
        return fileInfo;
    }

    /**
     * 修改收藏记录
     *
     * @param collectInfo 收藏记录
     * @return 结果
     */
    @Override
    public int updateCollectInfo(CollectInfo collectInfo) {
        return collectInfoMapper.updateCollectInfo(collectInfo);
    }

    /**
     * 批量删除收藏记录
     *
     * @param ids 需要删除的收藏记录主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteCollectInfoByIds(Long[] ids) {
        for (Long id : ids) {
            CollectInfo collectInfo = collectInfoMapper.selectCollectInfoById(id);
            updateFileInfoCollectCount(collectInfo);
        }
        return collectInfoMapper.deleteCollectInfoByIds(ids);
    }

    private void updateFileInfoCollectCount(CollectInfo collectInfo) {
        FileInfo file = fileInfoService.selectFileInfoById(collectInfo.getFileId());
        if (file != null) {
            file.setCollectCount(file.getCollectCount() - 1);
            if (file.getCollectCount() <= 0) {
                file.setCollectCount(0L);
            }
            fileInfoService.updateFileInfo(file);
        }
    }

    /**
     * 删除收藏记录信息
     *
     * @param id 收藏记录主键
     * @return 结果
     */
    @Override
    public int deleteCollectInfoById(Long id) {
        return collectInfoMapper.deleteCollectInfoById(id);
    }

    //endregion
    @Override
    public QueryWrapper<CollectInfo> getQueryWrapper(CollectInfoQuery collectInfoQuery) {
        QueryWrapper<CollectInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = collectInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = collectInfoQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        String folderName = collectInfoQuery.getFolderName();
        queryWrapper.like(StringUtils.isNotEmpty(folderName), "folder_name", folderName);

        Long fileTypeId = collectInfoQuery.getFileTypeId();
        queryWrapper.eq(StringUtils.isNotNull(fileTypeId), "file_type_id", fileTypeId);

        String fileTypeName = collectInfoQuery.getFileTypeName();
        queryWrapper.like(StringUtils.isNotEmpty(fileTypeName), "file_type_name", fileTypeName);

        Long fileId = collectInfoQuery.getFileId();
        queryWrapper.eq(StringUtils.isNotNull(fileId), "file_id", fileId);

        String fileName = collectInfoQuery.getFileName();
        queryWrapper.like(StringUtils.isNotEmpty(fileName), "file_name", fileName);

        String fileType = collectInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType), "file_type", fileType);

        Date createTime = collectInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<CollectInfoVo> convertVoList(List<CollectInfo> collectInfoList) {
        if (StringUtils.isEmpty(collectInfoList)) {
            return Collections.emptyList();
        }
        return collectInfoList.stream().map(CollectInfoVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入收藏记录数据
     *
     * @param collectInfoList 收藏记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importCollectInfoData(List<CollectInfo> collectInfoList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(collectInfoList) || collectInfoList.size() == 0) {
            throw new ServiceException("导入收藏记录数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (CollectInfo collectInfo : collectInfoList) {
            try {
                // 验证是否存在这个收藏记录
                Long id = collectInfo.getId();
                CollectInfo collectInfoExist = null;
                if (StringUtils.isNotNull(id)) {
                    collectInfoExist = collectInfoMapper.selectCollectInfoById(id);
                }
                if (StringUtils.isNull(collectInfoExist)) {
                    BeanValidators.validateWithException(validator, collectInfo);
                    collectInfo.setCreateTime(DateUtils.getNowDate());
                    collectInfoMapper.insertCollectInfo(collectInfo);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、收藏记录 " + idStr + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, collectInfo);
                    collectInfoMapper.updateCollectInfo(collectInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、收藏记录 " + id.toString() + " 更新成功");
                } else {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、收藏记录 " + idStr + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                Long id = collectInfo.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、收藏记录 " + idStr + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public int deleteCollectInfoByFileId(Long id) {
        //首先查询到这个记录
        CollectInfo collect = this.isCollect(id, SecurityUtils.getUserId());
        if (StringUtils.isNull(collect)) {
            throw new ServiceException("没有找到该收藏记录");
        }
        //查询到文件
        updateFileInfoCollectCount(collect);
        return collectInfoMapper.deleteCollectInfoById(collect.getId());
    }

}
