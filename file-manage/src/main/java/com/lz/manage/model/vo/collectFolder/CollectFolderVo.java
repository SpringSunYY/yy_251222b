package com.lz.manage.model.vo.collectFolder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.CollectFolder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 收藏夹Vo对象 tb_collect_folder
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class CollectFolderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long userId;
    private String userName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;


    /**
     * 对象转封装类
     *
     * @param collectFolder CollectFolder实体对象
     * @return CollectFolderVo
     */
    public static CollectFolderVo objToVo(CollectFolder collectFolder) {
        if (collectFolder == null) {
            return null;
        }
        CollectFolderVo collectFolderVo = new CollectFolderVo();
        BeanUtils.copyProperties(collectFolder, collectFolderVo);
        return collectFolderVo;
    }
}
