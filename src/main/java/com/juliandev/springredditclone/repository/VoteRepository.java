package com.juliandev.springredditclone.repository;

import com.juliandev.springredditclone.model.Post;
import com.juliandev.springredditclone.model.User;
import com.juliandev.springredditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
