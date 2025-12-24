package com.lz.manage.model.dto.collectInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.lz.manage.model.domain.CollectInfo;
/**
 * 收藏记录Query对象 tb_collect_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class CollectInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param collectInfoQuery 查询对象
     * @return CollectInfo
     */
    public static CollectInfo queryToObj(CollectInfoQuery collectInfoQuery) {
        if (collectInfoQuery == null) {
            return null;
        }
        CollectInfo collectInfo = new CollectInfo();
        BeanUtils.copyProperties(collectInfoQuery, collectInfo);
        return collectInfo;
    }
}
