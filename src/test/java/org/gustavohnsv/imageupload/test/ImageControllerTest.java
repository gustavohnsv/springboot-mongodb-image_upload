package org.gustavohnsv.imageupload.test;

import org.gustavohnsv.imageupload.config.SecurityConfig;
import org.gustavohnsv.imageupload.controller.ImageController;
import org.gustavohnsv.imageupload.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
@Import(SecurityConfig.class)
class ImageControllerTest {

    @MockBean
    private ImageService imageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    void testUploadImageSuccess() throws Exception {
        when(imageService.saveImage(any(), any(), eq(1.0))).thenReturn(true);

        mockMvc.perform(multipart("/api/images/image/")
                .file("file", "test content".getBytes())
                .param("filename", "filename.png")
                .param("resizeFactor", "1.0").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void testUploadImageFailureByAuth() throws Exception {
        when(imageService.saveImage(any(), any(), eq(1.0))).thenReturn(true);

        mockMvc.perform(multipart("/api/images/image/")
        .file("file", "test content".getBytes())
                .param("filename", "filename.png")
                .param("resizeFactor", "1.0").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    void testUploadImageFailureByToken() throws Exception {
        when(imageService.saveImage(any(), any(), eq(1.0))).thenReturn(true);

        mockMvc.perform(multipart("/api/images/image/")
        .file("file", "test content".getBytes())
                .param("filename", "filename.png")
                .param("resizeFactor", "1.0"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "a", password = "b", roles = "c")
    void testUploadImageFailureByUserPasswordRole() throws Exception {
        when(imageService.saveImage(any(), any(), eq(1.0))).thenReturn(true);

        mockMvc.perform(multipart("/api/images/image/")
                        .file("file", "test content".getBytes())
                        .param("filename", "filename.png")
                        .param("resizeFactor", "1.0"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    void testUploadImageFailureByFile() throws Exception {
        when(imageService.saveImage(any(), any(), eq(1.0))).thenReturn(true);

        mockMvc.perform(multipart("/api/images/image/")
                .file("file", new byte[0])
                .param("filename", "filename.png")
                .param("resizeFactor", "1.0").with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    void testUploadImageFailureByFilename() throws Exception {
        when(imageService.saveImage(any(), any(), eq(1.0))).thenReturn(false);

        String[] wrongNames = {"file.exe", "file..exe", "file.exe.png", ".exe"};

        for (String wrongName : wrongNames) {
            mockMvc.perform(multipart("/api/images/image/")
                            .file("file", "test content".getBytes())
                            .param("filename", wrongName)
                            .param("resizeFactor", "1.0").with(csrf()))
                    .andExpect(status().isBadRequest());
        }
    }

}