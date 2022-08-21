package com.moiz.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.User;
import com.moiz.reddit.Model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
	
	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
