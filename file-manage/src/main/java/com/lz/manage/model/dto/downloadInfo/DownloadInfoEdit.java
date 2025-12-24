package com.lz.manage.model.dto.downloadInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.DownloadInfo;
/**
 * 下载记录Vo对象 tb_download_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class DownloadInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类型编号 */
    private Long fileTypeId;

    /** 文件编号 */
    private Long fileId;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param downloadInfoEdit 编辑对象
     * @return DownloadInfo
     */
    public static DownloadInfo editToObj(DownloadInfoEdit downloadInfoEdit) {
        if (downloadInfoEdit == null) {
            return null;
        }
        DownloadInfo downloadInfo = new DownloadInfo();
        BeanUtils.copyProperties(downloadInfoEdit, downloadInfo);
        return downloadInfo;
    }
}
