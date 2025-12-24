package com.lz.manage.model.dto.fileInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.FileInfo;
/**
 * 文件信息Vo对象 tb_file_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class FileInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类型编号 */
    private Long fileTypeId;

    /** 名称 */
    private String fileName;

    /** 备注 */
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
