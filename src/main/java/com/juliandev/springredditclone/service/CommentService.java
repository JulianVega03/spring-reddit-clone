package com.juliandev.springredditclone.service;

import com.juliandev.springredditclone.dto.CommentsDto;
import com.juliandev.springredditclone.exceptions.PostNotFoundException;
import com.juliandev.springredditclone.mapper.CommentMapper;
import com.juliandev.springredditclone.model.Comment;
import com.juliandev.springredditclone.model.NotificationEmail;
import com.juliandev.springredditclone.model.Post;
import com.juliandev.springredditclone.model.User;
import com.juliandev.springredditclone.repository.CommentRepository;
import com.juliandev.springredditclone.repository.PostRepository;
import com.juliandev.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = authService.getCurrentUser().getUsername() + " posted a comment on your post." + POST_URL;
        mailService.sendMail(new NotificationEmail(authService.getCurrentUser().getUsername()+ " Commented on your post",authService.getCurrentUser().getEmail(), message));
    }



    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}