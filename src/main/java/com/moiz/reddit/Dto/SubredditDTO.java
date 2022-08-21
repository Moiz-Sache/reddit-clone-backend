package com.moiz.reddit.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDTO {

	private Long id;
	private String name;
	private String description;
	private Integer numberOfPosts;
}
