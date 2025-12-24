package com.lz.manage.model.dto.fileType;

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
import com.lz.manage.model.domain.FileType;
/**
 * 文件分类Query对象 tb_file_type
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class FileTypeQuery implements Serializable
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
     * @param fileTypeQuery 查询对象
     * @return FileType
     */
    public static FileType queryToObj(FileTypeQuery fileTypeQuery) {
        if (fileTypeQuery == null) {
            return null;
        }
        FileType fileType = new FileType();
        BeanUtils.copyProperties(fileTypeQuery, fileType);
        return fileType;
    }
}
