package com.lz.manage.model.dto.notification;

import com.lz.manage.model.domain.Notification;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 通知Vo对象 tb_notification
 *
 * @author YY
 * @date 2025-12-24
 */
@Data
public class NotificationEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;
    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否已读
     */
    private String readFlag;

    /**
     * 对象转封装类
     *
     * @param notificationEdit 编辑对象
     * @return Notification
     */
    public static Notification editToObj(NotificationEdit notificationEdit) {
        if (notificationEdit == null) {
            return null;
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationEdit, notification);
        return notification;
    }
}
