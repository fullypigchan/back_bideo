package com.app.bideo.controller.common;

import com.app.bideo.dto.gallery.SearchGalleryCoverResponseDTO;
import com.app.bideo.service.gallery.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final GalleryService galleryService;

    @GetMapping("/gallery-cover/{id}")
    public ResponseEntity<byte[]> galleryCover(@PathVariable Long id) {
        SearchGalleryCoverResponseDTO cover = galleryService.getSearchGalleryCover(id);
        if (cover == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, cover.getCacheControl())
                .contentType(MediaType.parseMediaType(cover.getContentType()))
                .body(cover.getBytes());
    }
}
