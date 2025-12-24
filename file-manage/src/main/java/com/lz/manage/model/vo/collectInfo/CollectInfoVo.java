package com.lz.manage.model.vo.collectInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.CollectInfo;
/**
 * 收藏记录Vo对象 tb_collect_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class CollectInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 收藏夹编号 */
    private Long folderId;

    /** 收藏夹 */
    private String folderName;

    /** 类型编号 */
    private Long fileTypeId;

    /** 类型名称 */
    private String fileTypeName;

    /** 文件编号 */
    private Long fileId;

    /** 文件名称 */
    private String fileName;

    /** 文件类型 */
    private String fileType;

    /** 文件大小 */
    private Long fileSize;

    /** 备注 */
    private String remark;

    /** 用户 */
    private Long userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param collectInfo CollectInfo实体对象
     * @return CollectInfoVo
     */
    public static CollectInfoVo objToVo(CollectInfo collectInfo) {
        if (collectInfo == null) {
            return null;
        }
        CollectInfoVo collectInfoVo = new CollectInfoVo();
        BeanUtils.copyProperties(collectInfo, collectInfoVo);
        return collectInfoVo;
    }
}
