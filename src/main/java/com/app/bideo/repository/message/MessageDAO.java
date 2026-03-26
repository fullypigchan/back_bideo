package com.app.bideo.repository.message;

import com.app.bideo.domain.message.MessageVO;
import com.app.bideo.dto.message.MessageResponseDTO;
import com.app.bideo.mapper.message.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageDAO {

    private final MessageMapper messageMapper;

    public void save(MessageVO messageVO) {
        messageMapper.insert(messageVO);
    }

    public List<MessageResponseDTO> findByRoomId(Long roomId, int offset, int limit) {
        return messageMapper.selectByRoomId(roomId, offset, limit);
    }

    public void markAllAsRead(Long roomId, Long memberId) {
        messageMapper.updateReadByRoomId(roomId, memberId);
    }

    public int getTotalUnreadCount(Long memberId) {
        return messageMapper.selectTotalUnreadCount(memberId);
    }
}
