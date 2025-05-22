package com.project.social_network.service;

import com.project.social_network.converter.PostConverter;
import com.project.social_network.dto.PostDto;
import com.project.social_network.exceptions.PostException;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.Post;
import com.project.social_network.model.User;
import com.project.social_network.repository.CommentRepository;
import com.project.social_network.repository.GroupRepository;
import com.project.social_network.repository.PostRepository;
import com.project.social_network.service.impl.PostServiceImpl;
import com.project.social_network.service.interfaces.UploadImageFile;
import com.project.social_network.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private PostConverter postConverter;

  @Mock
  private PostRepository postRepository;

  @Mock
  private UploadImageFile uploadImageFile;

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private PostServiceImpl postService;

  private User user;
  private Post post;
  private PostDto postDto;
  private MultipartFile file;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setFullName("John");

    post = new Post();
    post.setId(1L);
    post.setContent("Test post");
    post.setImage("http://image.url");
    post.setUser(user);

    postDto = new PostDto();
    postDto.setId(1L);
    postDto.setContent("Test post");
    postDto.setImage("http://image.url");

    file = mock(MultipartFile.class);
  }

  @Test
  void createPost_withValidData_returnsPostDto() throws IOException {
    String jwt = "valid-jwt";
    String content = "Test post";
    when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
    when(file.isEmpty()).thenReturn(false);
    when(uploadImageFile.uploadImage(file)).thenReturn("http://image.url");
    when(postConverter.postConverter(any(Post.class), eq(user))).thenReturn(post);
    when(postRepository.save(any(Post.class))).thenReturn(post);
    when(postConverter.toPostDto(eq(post), eq(user))).thenReturn(postDto);

    PostDto result = postService.createPost(content, file, jwt);

    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEqualTo("Test post");
    assertThat(result.getImage()).isEqualTo("http://image.url");
    verify(postRepository, times(1)).save(any(Post.class));
  }

  @Test
  void createPost_withEmptyFile_returnsPostDtoWithoutImage() throws IOException {
    String jwt = "valid-jwt";
    String content = "Test post";
    when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
    when(file.isEmpty()).thenReturn(true);
    when(postConverter.postConverter(any(Post.class), eq(user))).thenReturn(post);
    when(postRepository.save(any(Post.class))).thenReturn(post);
    when(postConverter.toPostDto(eq(post), eq(user))).thenReturn(postDto);

    PostDto result = postService.createPost(content, file, jwt);

    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEqualTo("Test post");
    assertThat(result.getImage()).isNull();
    verify(uploadImageFile, never()).uploadImage(any());
    verify(postRepository, times(1)).save(any(Post.class));
  }

  @Test
  void findById_whenPostExists_returnsPostDto() {
    Long postId = 1L;
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(postConverter.toPostDto(eq(post), eq(user))).thenReturn(postDto);

    PostDto result = postService.findById(postId);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(postId);
    assertThat(result.getContent()).isEqualTo("Test post");
  }

  @Test
  void findById_whenPostNotFound_throwsPostException() {
    Long postId = 1L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    PostException exception = assertThrows(PostException.class, () -> postService.findById(postId));
    assertThat(exception.getMessage()).isEqualTo("Post not found with id: " + postId);
  }

  @Test
  void deletePostById_whenUserIsOwner_deletesPost() {
    Long postId = 1L;
    Long userId = 1L;
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    doNothing().when(postRepository).deleteById(postId);

    postService.deletePostById(postId, userId);

    verify(postRepository, times(1)).deleteById(postId);
  }

  @Test
  void deletePostById_whenUserNotOwner_throwsUserException() {
    Long postId = 1L;
    Long userId = 2L;
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    UserException exception = assertThrows(UserException.class, () -> postService.deletePostById(postId, userId));
    assertThat(exception.getMessage()).isEqualTo("You can't delete another user's post");
    verify(postRepository, never()).deleteById(any());
  }

  @Test
  void updatePost_withValidData_returnsUpdatedPostDto() throws IOException {
    // Given
    Long postId = 1L;
    String jwt = "valid-jwt";
    String content = "Updated post";
    when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(file.isEmpty()).thenReturn(false);
    when(uploadImageFile.uploadImage(file)).thenReturn("http://new-image.url");
    when(postRepository.save(any(Post.class))).thenReturn(post);
    when(postConverter.toPostDto(eq(post), eq(user))).thenReturn(postDto);

    PostDto result = postService.updatePost(postId, file, content, jwt);

    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEqualTo("Test post"); // postDto vẫn giữ giá trị cũ
    assertThat(result.getImage()).isEqualTo("http://image.url"); // postDto giữ giá trị cũ
    verify(postRepository, times(1)).save(any(Post.class));
  }
}
