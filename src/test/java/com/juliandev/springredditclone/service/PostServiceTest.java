package com.juliandev.springredditclone.service;

import com.juliandev.springredditclone.dto.PostResponse;
import com.juliandev.springredditclone.mapper.PostMapper;
import com.juliandev.springredditclone.model.Post;
import com.juliandev.springredditclone.repository.PostRepository;
import com.juliandev.springredditclone.repository.SubredditRepository;
import com.juliandev.springredditclone.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;

class PostServiceTest {

    private final PostRepository postRepository = Mockito.mock(PostRepository.class);
    private final SubredditRepository subredditRepository = Mockito.mock(SubredditRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AuthService authService = Mockito.mock(AuthService.class);
    private final PostMapper postMapper = Mockito.mock(PostMapper.class);

    @Test
    @DisplayName("Encuentra un post por el ID")
    void ShouldFindPostById() {
        PostService postService = new PostService(postRepository,subredditRepository,userRepository,authService,postMapper);

        Post post = new Post(123L,"primer post", "https://youtube.com","description del primer test",0, null, Instant.now(),null);
        PostResponse postResponse = new PostResponse(123L,"primer post", "https://youtube.com","description del primer test",
                "test user","test subrredit", 0,0,"1 Hour Ago", false,false);
        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(postResponse);

        PostResponse actualResponse = postService.getPost(123L);
        Assertions.assertThat(actualResponse.getId()).isEqualTo(postResponse.getId());
        Assertions.assertThat(actualResponse.getPostName()).isEqualTo(postResponse.getPostName());
    }

}