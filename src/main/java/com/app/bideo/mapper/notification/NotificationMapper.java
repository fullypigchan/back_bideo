package com.app.bideo.mapper.notification;

import com.app.bideo.domain.notification.NotificationVO;
import com.app.bideo.dto.notification.NotificationResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void insert(NotificationVO notificationVO);

    List<NotificationResponseDTO> selectByMemberId(Long memberId);
}
