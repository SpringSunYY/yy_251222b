package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.DataScope;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.manage.mapper.CollectFolderMapper;
import com.lz.manage.model.domain.CollectFolder;
import com.lz.manage.model.dto.collectFolder.CollectFolderQuery;
import com.lz.manage.model.vo.collectFolder.CollectFolderVo;
import com.lz.manage.service.ICollectFolderService;
import com.lz.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收藏夹Service业务层处理
 *
 * @author YY
 * @date 2025-12-24
 */
@Service
public class CollectFolderServiceImpl extends ServiceImpl<CollectFolderMapper, CollectFolder> implements ICollectFolderService {
    private static final Logger log = LoggerFactory.getLogger(CollectFolderServiceImpl.class);

    /**
     * 导入用户数据校验器
     */
    private static Validator validator;

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private CollectFolderMapper collectFolderMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码

    /**
     * 查询收藏夹
     *
     * @param id 收藏夹主键
     * @return 收藏夹
     */
    @Override
    public CollectFolder selectCollectFolderById(Long id) {
        return collectFolderMapper.selectCollectFolderById(id);
    }

    /**
     * 查询收藏夹列表
     *
     * @param collectFolder 收藏夹
     * @return 收藏夹
     */
    @DataScope(deptAlias = "tb_collect_folder", userAlias = "tb_collect_folder")
    @Override
    public List<CollectFolder> selectCollectFolderList(CollectFolder collectFolder) {
        List<CollectFolder> collectFolders = collectFolderMapper.selectCollectFolderList(collectFolder);
        for (CollectFolder info : collectFolders) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
        }
        return collectFolders;
    }

    /**
     * 新增收藏夹
     *
     * @param collectFolder 收藏夹
     * @return 结果
     */
    @Override
    public int insertCollectFolder(CollectFolder collectFolder) {
        collectFolder.setUserId(SecurityUtils.getUserId());
        collectFolder.setCreateTime(DateUtils.getNowDate());
        return collectFolderMapper.insertCollectFolder(collectFolder);
    }

    /**
     * 修改收藏夹
     *
     * @param collectFolder 收藏夹
     * @return 结果
     */
    @Override
    public int updateCollectFolder(CollectFolder collectFolder) {
        collectFolder.setUpdateTime(DateUtils.getNowDate());
        return collectFolderMapper.updateCollectFolder(collectFolder);
    }

    /**
     * 批量删除收藏夹
     *
     * @param ids 需要删除的收藏夹主键
     * @return 结果
     */
    @Override
    public int deleteCollectFolderByIds(Long[] ids) {
        return collectFolderMapper.deleteCollectFolderByIds(ids);
    }

    /**
     * 删除收藏夹信息
     *
     * @param id 收藏夹主键
     * @return 结果
     */
    @Override
    public int deleteCollectFolderById(Long id) {
        return collectFolderMapper.deleteCollectFolderById(id);
    }

    //endregion
    @Override
    public QueryWrapper<CollectFolder> getQueryWrapper(CollectFolderQuery collectFolderQuery) {
        QueryWrapper<CollectFolder> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = collectFolderQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = collectFolderQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        String name = collectFolderQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        Date createTime = collectFolderQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<CollectFolderVo> convertVoList(List<CollectFolder> collectFolderList) {
        if (StringUtils.isEmpty(collectFolderList)) {
            return Collections.emptyList();
        }
        return collectFolderList.stream().map(CollectFolderVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入收藏夹数据
     *
     * @param collectFolderList 收藏夹数据列表
     * @param isUpdateSupport   是否更新支持，如果已存在，则进行更新数据
     * @param operName          操作用户
     * @return 结果
     */
    @Override
    public String importCollectFolderData(List<CollectFolder> collectFolderList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(collectFolderList) || collectFolderList.size() == 0) {
            throw new ServiceException("导入收藏夹数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (CollectFolder collectFolder : collectFolderList) {
            try {
                // 验证是否存在这个收藏夹
                Long id = collectFolder.getId();
                CollectFolder collectFolderExist = null;
                if (StringUtils.isNotNull(id)) {
                    collectFolderExist = collectFolderMapper.selectCollectFolderById(id);
                }
                if (StringUtils.isNull(collectFolderExist)) {
                    BeanValidators.validateWithException(validator, collectFolder);
                    collectFolder.setCreateTime(DateUtils.getNowDate());
                    collectFolderMapper.insertCollectFolder(collectFolder);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、收藏夹 " + idStr + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, collectFolder);
                    collectFolder.setUpdateTime(DateUtils.getNowDate());
                    collectFolderMapper.updateCollectFolder(collectFolder);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、收藏夹 " + id.toString() + " 更新成功");
                } else {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、收藏夹 " + idStr + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                Long id = collectFolder.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、收藏夹 " + idStr + " 导入失败：";
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

}
