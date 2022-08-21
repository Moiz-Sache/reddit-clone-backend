package com.moiz.reddit.Model;

import java.util.Arrays;

import com.moiz.reddit.Exceptions.SpringRedditEception;

public enum VoteType {

	UPVOTE(1),DOWNVOTE(-1);
	
	private int direction;
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	VoteType(int direction){
		
	}
	
	public static VoteType lookup(Integer direction) {
		return Arrays.stream(VoteType.values())
				.filter(value -> value.getDirection() == direction)
				.findAny()
				.orElseThrow(() -> new SpringRedditEception("Vote not founnd"));
	}
	
	
	
}
