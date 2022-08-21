package com.moiz.reddit.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.moiz.reddit.Dto.SubredditDTO;
import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
	SubredditDTO mapSubredditToDto(Subreddit subreddit);
	
	default Integer mapPosts(List<Post> numberOfPosts) {
		return numberOfPosts.size();
	};
	
	@InheritInverseConfiguration
	@Mapping(target = "posts", ignore = true)
	Subreddit mapDtoToSubreddit(SubredditDTO subredditDTO);
}
