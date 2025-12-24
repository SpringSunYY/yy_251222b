package com.lz.manage.model.dto.collectFolder;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.CollectFolder;
/**
 * 收藏夹Vo对象 tb_collect_folder
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class CollectFolderEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 名称 */
    private String name;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private Long userId;

    /**
     * 对象转封装类
     *
     * @param collectFolderEdit 编辑对象
     * @return CollectFolder
     */
    public static CollectFolder editToObj(CollectFolderEdit collectFolderEdit) {
        if (collectFolderEdit == null) {
            return null;
        }
        CollectFolder collectFolder = new CollectFolder();
        BeanUtils.copyProperties(collectFolderEdit, collectFolder);
        return collectFolder;
    }
}
