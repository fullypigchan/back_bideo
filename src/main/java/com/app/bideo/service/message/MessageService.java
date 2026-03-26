package com.app.bideo.service.message;

import com.app.bideo.domain.member.MemberVO;
import com.app.bideo.domain.message.MessageRoomVO;
import com.app.bideo.domain.message.MessageVO;
import com.app.bideo.dto.member.MemberListResponseDTO;
import com.app.bideo.dto.message.*;
import com.app.bideo.repository.member.MemberRepository;
import com.app.bideo.repository.message.MessageDAO;
import com.app.bideo.repository.message.MessageRoomDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MessageService {

    private final MessageDAO messageDAO;
    private final MessageRoomDAO messageRoomDAO;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final int PAGE_SIZE = 50;

    @Transactional(readOnly = true)
    public List<MessageRoomResponseDTO> getMyRooms(Long memberId) {
        List<MessageRoomResponseDTO> rooms = messageRoomDAO.findRoomsByMemberId(memberId);
        for (MessageRoomResponseDTO room : rooms) {
            List<MemberListResponseDTO> members = messageRoomDAO.findRoomMembers(room.getId(), memberId);
            room.setMembers(members);
        }
        return rooms;
    }

    public MessageRoomResponseDTO createOrGetRoom(Long currentMemberId, MessageRoomCreateRequestDTO dto) {
        if (dto.getMemberIds() == null || dto.getMemberIds().isEmpty()) {
            throw new IllegalArgumentException("상대방을 선택해주세요.");
        }

        Long partnerId = dto.getMemberIds().get(0);
        if (partnerId.equals(currentMemberId)) {
            throw new IllegalArgumentException("자기 자신에게 메시지를 보낼 수 없습니다.");
        }

        Long existingRoomId = messageRoomDAO.findExistingRoomId(currentMemberId, partnerId);
        if (existingRoomId != null) {
            return buildRoomResponse(existingRoomId, currentMemberId);
        }

        MessageRoomVO roomVO = new MessageRoomVO();
        messageRoomDAO.createRoom(roomVO);

        messageRoomDAO.addMember(roomVO.getId(), currentMemberId);
        messageRoomDAO.addMember(roomVO.getId(), partnerId);

        return buildRoomResponse(roomVO.getId(), currentMemberId);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessages(Long roomId, Long currentMemberId, int page) {
        if (!messageRoomDAO.isMember(roomId, currentMemberId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        return messageDAO.findByRoomId(roomId, page * PAGE_SIZE, PAGE_SIZE);
    }

    public MessageResponseDTO sendMessage(Long roomId, Long senderId, String content) {
        if (!messageRoomDAO.isMember(roomId, senderId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        MessageVO messageVO = MessageVO.builder()
                .messageRoomId(roomId)
                .senderId(senderId)
                .content(content)
                .build();
        messageDAO.save(messageVO);
        messageRoomDAO.updateTimestamp(roomId);

        MemberVO sender = memberRepository.findById(senderId).orElse(null);
        String senderNickname = sender != null ? sender.getNickname() : "";
        String senderProfileImage = sender != null ? sender.getProfileImage() : null;

        MessageResponseDTO response = MessageResponseDTO.builder()
                .id(messageVO.getId())
                .senderId(senderId)
                .senderNickname(senderNickname)
                .senderProfileImage(senderProfileImage)
                .content(content)
                .isRead(false)
                .createdDatetime(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/room." + roomId, response);

        return response;
    }

    public void markAsRead(Long roomId, Long memberId) {
        if (!messageRoomDAO.isMember(roomId, memberId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        messageDAO.markAllAsRead(roomId, memberId);
    }

    @Transactional(readOnly = true)
    public int getTotalUnreadCount(Long memberId) {
        return messageDAO.getTotalUnreadCount(memberId);
    }

    @Transactional(readOnly = true)
    public List<MemberListResponseDTO> searchMembers(String keyword, Long currentMemberId) {
        return memberRepository.searchByKeyword(keyword, currentMemberId, 20);
    }

    private MessageRoomResponseDTO buildRoomResponse(Long roomId, Long currentMemberId) {
        List<MemberListResponseDTO> members = messageRoomDAO.findRoomMembers(roomId, currentMemberId);
        return MessageRoomResponseDTO.builder()
                .id(roomId)
                .members(members)
                .build();
    }
}
