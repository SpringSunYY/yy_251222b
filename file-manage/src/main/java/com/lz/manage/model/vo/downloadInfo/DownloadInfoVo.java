package com.lz.manage.model.vo.downloadInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.DownloadInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 下载记录Vo对象 tb_download_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class DownloadInfoVo implements Serializable {
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
     * 类型名称
     */
    private String fileTypeName;

    /**
     * 文件编号
     */
    private Long fileId;

    /**
     * 文件名称
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
     * 备注
     */
    private String remark;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    /**
     * 对象转封装类
     *
     * @param downloadInfo DownloadInfo实体对象
     * @return DownloadInfoVo
     */
    public static DownloadInfoVo objToVo(DownloadInfo downloadInfo) {
        if (downloadInfo == null) {
            return null;
        }
        DownloadInfoVo downloadInfoVo = new DownloadInfoVo();
        BeanUtils.copyProperties(downloadInfo, downloadInfoVo);
        return downloadInfoVo;
    }
}
