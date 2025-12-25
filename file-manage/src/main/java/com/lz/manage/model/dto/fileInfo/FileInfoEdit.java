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
public class FileInfoEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 类型编号
     */
    private Long fileTypeId;

    /**
     * 文件
     */
    private String fileUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 名称
     */
    private String fileName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param fileInfoEdit 编辑对象
     * @return FileInfo
     */
    public static FileInfo editToObj(FileInfoEdit fileInfoEdit) {
        if (fileInfoEdit == null) {
            return null;
        }
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(fileInfoEdit, fileInfo);
        return fileInfo;
    }
}
