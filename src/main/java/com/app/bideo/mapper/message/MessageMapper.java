package com.app.bideo.mapper.message;

import com.app.bideo.domain.message.MessageVO;
import com.app.bideo.dto.message.MessageResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    void insert(MessageVO messageVO);

    List<MessageResponseDTO> selectByRoomId(@Param("roomId") Long roomId,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    void updateReadByRoomId(@Param("roomId") Long roomId,
                            @Param("memberId") Long memberId);

    int selectTotalUnreadCount(@Param("memberId") Long memberId);
}
