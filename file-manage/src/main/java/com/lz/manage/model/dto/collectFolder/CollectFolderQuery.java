package com.lz.manage.model.dto.collectFolder;

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
import com.lz.manage.model.domain.CollectFolder;
/**
 * 收藏夹Query对象 tb_collect_folder
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class CollectFolderQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 名称 */
    private String name;

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
     * @param collectFolderQuery 查询对象
     * @return CollectFolder
     */
    public static CollectFolder queryToObj(CollectFolderQuery collectFolderQuery) {
        if (collectFolderQuery == null) {
            return null;
        }
        CollectFolder collectFolder = new CollectFolder();
        BeanUtils.copyProperties(collectFolderQuery, collectFolder);
        return collectFolder;
    }
}
