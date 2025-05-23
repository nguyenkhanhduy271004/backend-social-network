package com.project.social_network.repository;

import com.project.social_network.model.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class PostRepositoryTest {

  @Autowired
  PostRepository postRepository;

  @Autowired
  TestEntityManager entityManager;

  private Post mockPost() {
    Post post = new Post();
    post.setContent("Test");

    return post;
  }

  @Test
  void testSave_WhenSavePost_ReturnPost() {
    Post post = mockPost();

    entityManager.persistAndFlush(post);

    Post postSaved = postRepository.save(post);

    Assertions.assertEquals(post.getId(), postSaved.getId());
    Assertions.assertEquals(post.getContent(), postSaved.getContent());
  }


}
