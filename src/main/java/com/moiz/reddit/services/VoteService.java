package com.moiz.reddit.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moiz.reddit.Dto.VoteDto;
import com.moiz.reddit.Exceptions.SpringRedditEception;
import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.Vote;
import com.moiz.reddit.Model.VoteType;
import com.moiz.reddit.repository.PostRepository;
import com.moiz.reddit.repository.VoteRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	
	
	@Transactional
	public void vote(VoteDto voteDto) {
		
		Post post = postRepository.findById(voteDto.getPostId())
				.orElseThrow(() -> new SpringRedditEception("Post not found ID : "+voteDto.getPostId()));
		
		Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		
		if(voteByPostAndUser.isPresent() && 
				voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
			throw new SpringRedditEception("You Have already "+voteDto.getVoteType()+" for this post");
		}
		
		if(VoteType.UPVOTE.equals(voteDto.getVoteType())) {
			post.setVoteCount(post.getVoteCount() + 1);
		}else {
			post.setVoteCount(post.getVoteCount() - 1);
		}
		
		voteRepository.save(mapToVote(voteDto, post));
		postRepository.save(post);
	}


	private Vote mapToVote(VoteDto voteDto, Post post) {
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.post(post)
				.user(authService.getCurrentUser())
				.build();
	}

}
