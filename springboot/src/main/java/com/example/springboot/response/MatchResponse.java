package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.MatchVO;

public class MatchResponse extends Response{
	private List<MatchVO> matches = new ArrayList<MatchVO>();

	public List<MatchVO> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchVO> matches) {
		this.matches = matches;
	}

	public MatchResponse(ResponseType responseType) {
		super(responseType);
	}
	public MatchResponse(ResponseType responseType, List<MatchVO> matches) {
		super(responseType);
		this.matches = matches;
	}

	public MatchResponse() {
		super();
	}

	
}
