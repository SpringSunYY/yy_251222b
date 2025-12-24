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
public class FileTypeEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 名称 */
    private String name;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param fileTypeEdit 编辑对象
     * @return FileType
     */
    public static FileType editToObj(FileTypeEdit fileTypeEdit) {
        if (fileTypeEdit == null) {
            return null;
        }
        FileType fileType = new FileType();
        BeanUtils.copyProperties(fileTypeEdit, fileType);
        return fileType;
    }
}
