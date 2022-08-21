package com.moiz.reddit.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moiz.reddit.Dto.SubredditDTO;
import com.moiz.reddit.Exceptions.SpringRedditEception;
import com.moiz.reddit.Model.Subreddit;
import com.moiz.reddit.mapper.SubredditMapper;
import com.moiz.reddit.repository.SubredditRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditService {

	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDTO save(SubredditDTO subredditDTO) {
		Subreddit save= subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDTO));
		subredditDTO.setId(save.getId());
		return subredditDTO;
	}

	@Transactional(readOnly = true)
	public List<SubredditDTO> getAll() {
		return subredditRepository.findAll()
			.stream()
			.map(subredditMapper::mapSubredditToDto)
			.collect(Collectors.toList());
		
	}
	
	public SubredditDTO getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditEception("No subreddit with id : "+ id));
		return subredditMapper.mapSubredditToDto(subreddit);
	}
}
