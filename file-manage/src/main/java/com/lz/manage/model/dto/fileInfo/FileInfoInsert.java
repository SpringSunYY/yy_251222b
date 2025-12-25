package com.lz.manage.model.dto.fileInfo;

import com.lz.manage.model.domain.FileInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 文件信息Vo对象 tb_file_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class FileInfoInsert implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类型编号
     */
    private Long fileTypeId;

    /**
     * 名称
     */
    private String fileName;

    /**
     * 文件
     */
    private String fileUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param fileInfoInsert 插入对象
     * @return FileInfoInsert
     */
    public static FileInfo insertToObj(FileInfoInsert fileInfoInsert) {
        if (fileInfoInsert == null) {
            return null;
        }
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(fileInfoInsert, fileInfo);
        return fileInfo;
    }
}
