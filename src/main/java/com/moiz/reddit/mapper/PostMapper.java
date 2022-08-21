package com.moiz.reddit.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.moiz.reddit.Dto.PostsRequest;
import com.moiz.reddit.Dto.PostsResponse;
import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.Subreddit;
import com.moiz.reddit.Model.User;
import com.moiz.reddit.Model.Vote;
import com.moiz.reddit.Model.VoteType;
import com.moiz.reddit.repository.CommentRepository;
import com.moiz.reddit.repository.VoteRepository;
import com.moiz.reddit.services.AuthService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
	
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private AuthService authService;

	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "description", source = "postsRequest.description")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "voteCount", constant = "0")
	@Mapping(target = "user", source = "user")
	public abstract Post map(PostsRequest postsRequest, Subreddit subreddit, User user);
	
	@Mapping(target = "id", source = "postId")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "userName", source = "user.username")
	@Mapping(target = "commentCount", expression = "java(commentCount(post))")
	@Mapping(target = "duration", expression = "java(getDuration(post))")
	@Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
	public abstract PostsResponse mapToDto(Post post);
	
	Integer commentCount(Post post) {
		return commentRepository.findByPost(post).size();
	}
	
	String getDuration(Post post) {
		return TimeAgo.using(post.getCreatedDate().toEpochMilli());
	}
	
	boolean isPostUpVoted(Post post) {
        return checkVoteType(post, VoteType.UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                    authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
