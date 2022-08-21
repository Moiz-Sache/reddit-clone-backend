package com.moiz.reddit.Dto;

import com.moiz.reddit.Model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {

	private VoteType voteType;
	private Long postId;
}
