package com.moiz.reddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moiz.reddit.Model.Comment;
import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPost(Post post);

	List<Comment> findAllByUser(User user);

}
