package com.project.social_network.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.social_network.dto.PostDto;
import com.project.social_network.service.interfaces.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private PostService postService;

  @Test
  @WithMockUser(username = "testuser", roles = {"USER"})
  void createPost() throws Exception {
    // Given: Thiết lập dữ liệu giả lập
    PostDto postDto = new PostDto();
    postDto.setId(1L);
    postDto.setContent("Test post");
    postDto.setImage(null);

    // Mock hành vi của PostService
    when(postService.createPost(anyString(), any(), anyString())).thenReturn(postDto);

    // Tạo MockMultipartFile (tùy chọn, nếu gửi file)
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "image.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "test image content".getBytes()
    );

    // When: Gửi yêu cầu POST
    mockMvc.perform(multipart("/api/posts/create")
            .file(file) // Gửi file (có thể bỏ nếu không cần)
            .param("content", "Test post") // Gửi content
            .header("Authorization", "Bearer valid-jwt") // Gửi JWT
            .contentType(MediaType.MULTIPART_FORM_DATA))
        // Then: Kiểm tra phản hồi
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.message").value("Create post successfully"))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.content").value("Test post"))
        .andExpect(jsonPath("$.data.image").isEmpty());
  }
}