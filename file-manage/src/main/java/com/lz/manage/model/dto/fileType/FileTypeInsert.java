package com.lz.manage.model.dto.fileType;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.FileType;
/**
 * 文件分类Vo对象 tb_file_type
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class FileTypeInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 名称 */
    private String name;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param fileTypeInsert 插入对象
     * @return FileTypeInsert
     */
    public static FileType insertToObj(FileTypeInsert fileTypeInsert) {
        if (fileTypeInsert == null) {
            return null;
        }
        FileType fileType = new FileType();
        BeanUtils.copyProperties(fileTypeInsert, fileType);
        return fileType;
    }
}
