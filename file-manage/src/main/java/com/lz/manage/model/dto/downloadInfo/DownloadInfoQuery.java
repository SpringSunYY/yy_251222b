package com.lz.manage.model.dto.downloadInfo;

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
import com.lz.manage.model.domain.DownloadInfo;
/**
 * 下载记录Query对象 tb_download_info
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class DownloadInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类型名称 */
    private String fileTypeName;

    /** 文件名称 */
    private String fileName;

    /** 文件类型 */
    private String fileType;

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
     * @param downloadInfoQuery 查询对象
     * @return DownloadInfo
     */
    public static DownloadInfo queryToObj(DownloadInfoQuery downloadInfoQuery) {
        if (downloadInfoQuery == null) {
            return null;
        }
        DownloadInfo downloadInfo = new DownloadInfo();
        BeanUtils.copyProperties(downloadInfoQuery, downloadInfo);
        return downloadInfo;
    }
}
