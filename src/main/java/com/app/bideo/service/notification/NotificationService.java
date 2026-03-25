package com.app.bideo.service.notification;

import com.app.bideo.domain.notification.NotificationVO;
import com.app.bideo.dto.notification.NotificationResponseDTO;
import com.app.bideo.mapper.notification.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationMapper notificationMapper;

    public List<NotificationResponseDTO> getNotifications(Long memberId) {
        return notificationMapper.selectByMemberId(memberId);
    }

    @Transactional
    public void createNotification(NotificationVO notificationVO) {
        notificationMapper.insert(notificationVO);
    }
}
