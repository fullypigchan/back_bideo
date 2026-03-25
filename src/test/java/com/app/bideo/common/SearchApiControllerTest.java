package com.app.bideo.common;

import com.app.bideo.controller.common.ImageController;
import com.app.bideo.controller.common.SearchAPIController;
import com.app.bideo.dto.gallery.SearchGalleryCoverResponseDTO;
import com.app.bideo.dto.gallery.SearchGallerySuggestionDTO;
import com.app.bideo.service.common.SearchHistoryService;
import com.app.bideo.service.gallery.GalleryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchApiControllerTest {

    private MockMvc mockMvc;
    private SearchHistoryService searchHistoryService;
    private GalleryService galleryService;

    @BeforeEach
    void setUp() {
        searchHistoryService = Mockito.mock(SearchHistoryService.class);
        galleryService = Mockito.mock(GalleryService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new SearchAPIController(searchHistoryService, galleryService),
                        new ImageController(galleryService))
                .build();
    }

    @Test
    void recommendedGalleriesExposeLightweightSuggestionShape() throws Exception {
        given(galleryService.getRecommendedSearchGalleries()).willReturn(List.of(
                SearchGallerySuggestionDTO.builder()
                        .id(7L)
                        .title("봄 전시")
                        .coverImageUrl("/image/gallery-cover/7")
                        .hasCoverImage(true)
                        .build()
        ));

        mockMvc.perform(get("/api/search/recommended-galleries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[0].title").value("봄 전시"))
                .andExpect(jsonPath("$[0].coverImageUrl").value("/image/gallery-cover/7"))
                .andExpect(jsonPath("$[0].hasCoverImage").value(true))
                .andExpect(jsonPath("$[0].coverImage").doesNotExist())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("data:image"))));
    }

    @Test
    void galleryCoverEndpointStreamsBinaryImageResponse() throws Exception {
        byte[] imageBytes = "fake-image".getBytes(StandardCharsets.UTF_8);
        given(galleryService.getSearchGalleryCover(11L)).willReturn(
                SearchGalleryCoverResponseDTO.builder()
                        .contentType(MediaType.IMAGE_PNG_VALUE)
                        .bytes(imageBytes)
                        .cacheControl("public, max-age=300")
                        .build()
        );

        mockMvc.perform(get("/image/gallery-cover/11"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "public, max-age=300"))
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageBytes));
    }
}
