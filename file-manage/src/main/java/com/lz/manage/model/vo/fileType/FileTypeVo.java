package com.lz.manage.model.vo.fileType;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.FileType;
/**
 * 文件分类Vo对象 tb_file_type
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class FileTypeVo implements Serializable
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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;


     /**
     * 对象转封装类
     *
     * @param fileType FileType实体对象
     * @return FileTypeVo
     */
    public static FileTypeVo objToVo(FileType fileType) {
        if (fileType == null) {
            return null;
        }
        FileTypeVo fileTypeVo = new FileTypeVo();
        BeanUtils.copyProperties(fileType, fileTypeVo);
        return fileTypeVo;
    }
}
