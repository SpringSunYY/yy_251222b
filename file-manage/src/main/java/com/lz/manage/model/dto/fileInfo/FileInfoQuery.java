package com.lz.manage.model.dto.fileInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.manage.model.domain.FileInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 文件信息Query对象 tb_file_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class FileInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 类型名称
     */
    private String fileTypeName;

    /**
     * 名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 是否公开
     */
    private String isPublic;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param fileInfoQuery 查询对象
     * @return FileInfo
     */
    public static FileInfo queryToObj(FileInfoQuery fileInfoQuery) {
        if (fileInfoQuery == null) {
            return null;
        }
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(fileInfoQuery, fileInfo);
        return fileInfo;
    }
}
