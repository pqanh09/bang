package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.MatchVO;

public class MatchResponse extends Response{
	private String matchId;
	private List<MatchVO> matches = new ArrayList<MatchVO>();
	private boolean host = false;

	
	public boolean isHost() {
		return host;
	}

	public void setHost(boolean host) {
		this.host = host;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public List<MatchVO> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchVO> matches) {
		this.matches = matches;
	}

	public MatchResponse(ResponseType responseType, String matchId, boolean host) {
		super(responseType);
		this.matchId = matchId;
		this.host = host;
	}
	public MatchResponse(ResponseType responseType, List<MatchVO> matches) {
		super(responseType);
		this.matches = matches;
	}

	public MatchResponse() {
		super();
	}

	
}
