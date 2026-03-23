package com.app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class TemplateCleanupTest {

    @Test
    void loginAndSignupStandaloneTemplatesAreRemovedWhileModalAssetsStayReferenced() throws IOException {
        assertFalse(resourceExists("templates/main/login.html"));
        assertFalse(resourceExists("templates/main/sign-up.html"));

        String mainTemplate = readResource("templates/main/main.html");
        String layoutTemplate = readResource("templates/common/layout.html");
        String introTemplate = readResource("templates/main/intro-main.html");

        assertTrue(mainTemplate.contains("layout:decorate=\"~{/common/layout}\""));

        assertTrue(layoutTemplate.contains("/css/main/modal-shared.css"));
        assertTrue(layoutTemplate.contains("/css/main/auth-modal.css"));
        assertTrue(layoutTemplate.contains("/css/main/signup-modal.css"));
        assertTrue(layoutTemplate.contains("/js/main/modal-shared.js"));
        assertTrue(layoutTemplate.contains("/js/main/auth-modal.js"));
        assertTrue(layoutTemplate.contains("/js/main/signup-modal.js"));

        assertTrue(introTemplate.contains("/css/main/modal-shared.css"));
        assertTrue(introTemplate.contains("/css/main/auth-modal.css"));
        assertTrue(introTemplate.contains("/css/main/signup-modal.css"));
        assertTrue(introTemplate.contains("/js/main/modal-shared.js"));
        assertTrue(introTemplate.contains("/js/main/auth-modal.js"));
        assertTrue(introTemplate.contains("/js/main/signup-modal.js"));
    }

    private boolean resourceExists(String path) {
        return new ClassPathResource(path).exists();
    }

    private String readResource(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        assertNotNull(resource);
        try (InputStream stream = resource.getInputStream()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
