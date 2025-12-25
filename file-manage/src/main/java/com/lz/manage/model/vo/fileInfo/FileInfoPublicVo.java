package com.lz.manage.model.vo.fileInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.FileInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息Vo对象 tb_file_info
 * 公开图片
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class FileInfoPublicVo implements Serializable {
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
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件
     */
    private String fileUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 浏览次数
     */
    private Long viewCount;

    /**
     * 下载次数
     */
    private Long downloadCount;

    /**
     * 收藏次数
     */
    private Long collectCount;

    /**
     * 是否收藏
     */
    private Boolean isCollect;

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
     * 对象转封装类
     *
     * @param fileInfo FileInfo实体对象
     * @return FileInfoVo
     */
    public static FileInfoPublicVo objToVo(FileInfo fileInfo) {
        if (fileInfo == null) {
            return null;
        }
        FileInfoPublicVo fileInfoVo = new FileInfoPublicVo();
        BeanUtils.copyProperties(fileInfo, fileInfoVo);
        return fileInfoVo;
    }
}
