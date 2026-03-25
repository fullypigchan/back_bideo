package com.app.bideo.gallery;

import com.app.bideo.controller.gallery.GalleryAPIController;
import com.app.bideo.controller.member.MemberFollowAPIController;
import com.app.bideo.dto.gallery.GalleryBookmarkStateResponseDTO;
import com.app.bideo.dto.gallery.GalleryCardResponseDTO;
import com.app.bideo.dto.gallery.GalleryLikeStateResponseDTO;
import com.app.bideo.dto.member.MemberFollowStateResponseDTO;
import com.app.bideo.service.gallery.GalleryService;
import com.app.bideo.service.member.MemberFollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GalleryInteractionControllerTest {

    private MockMvc mockMvc;
    private GalleryService galleryService;
    private MemberFollowService memberFollowService;

    @BeforeEach
    void setUp() {
        galleryService = Mockito.mock(GalleryService.class);
        memberFollowService = Mockito.mock(MemberFollowService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new GalleryAPIController(galleryService),
                new MemberFollowAPIController(memberFollowService)
        ).build();
    }

    @Test
    void galleryInteractionEndpointsExposeMinimalStatePayloads() throws Exception {
        given(galleryService.likeGallery(7L)).willReturn(
                GalleryLikeStateResponseDTO.builder()
                        .liked(true)
                        .likeCount(14)
                        .build()
        );
        given(galleryService.removeGalleryLike(7L)).willReturn(
                GalleryLikeStateResponseDTO.builder()
                        .liked(false)
                        .likeCount(13)
                        .build()
        );
        given(galleryService.bookmarkGallery(7L)).willReturn(
                GalleryBookmarkStateResponseDTO.builder()
                        .bookmarked(true)
                        .build()
        );
        given(galleryService.removeGalleryBookmark(7L)).willReturn(
                GalleryBookmarkStateResponseDTO.builder()
                        .bookmarked(false)
                        .build()
        );
        given(galleryService.getSimilarGalleries(7L, 4)).willReturn(List.of(
                GalleryCardResponseDTO.builder()
                        .id(11L)
                        .title("봄 전시")
                        .memberNickname("curator")
                        .coverImageUrl("/image/gallery-cover/11")
                        .hasCoverImage(true)
                        .build()
        ));

        mockMvc.perform(post("/api/galleries/7/likes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liked").value(true))
                .andExpect(jsonPath("$.likeCount").value(14));

        mockMvc.perform(delete("/api/galleries/7/likes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liked").value(false))
                .andExpect(jsonPath("$.likeCount").value(13));

        mockMvc.perform(post("/api/galleries/7/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmarked").value(true));

        mockMvc.perform(delete("/api/galleries/7/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmarked").value(false));

        mockMvc.perform(get("/api/galleries/7/similar").param("limit", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(11))
                .andExpect(jsonPath("$[0].title").value("봄 전시"))
                .andExpect(jsonPath("$[0].memberNickname").value("curator"))
                .andExpect(jsonPath("$[0].coverImageUrl").value("/image/gallery-cover/11"))
                .andExpect(jsonPath("$[0].hasCoverImage").value(true));
    }

    @Test
    void memberFollowEndpointExposesFollowingState() throws Exception {
        given(memberFollowService.follow(33L)).willReturn(
                MemberFollowStateResponseDTO.builder()
                        .following(true)
                        .build()
        );
        given(memberFollowService.unfollow(33L)).willReturn(
                MemberFollowStateResponseDTO.builder()
                        .following(false)
                        .build()
        );

        mockMvc.perform(post("/api/members/33/follow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.following").value(true));

        mockMvc.perform(delete("/api/members/33/follow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.following").value(false));
    }
}
