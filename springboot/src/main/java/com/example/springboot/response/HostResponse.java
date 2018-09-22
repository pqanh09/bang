package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.MatchVO;

public class HostResponse extends UserResponse{
	private String matchId;
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

	public HostResponse(ResponseType responseType, String userName, String matchId, boolean host) {
		super(responseType, userName);
		this.matchId = matchId;
		this.host = host;
	}
	public HostResponse(ResponseType responseType, String userName) {
		super(responseType, userName);
	}

	public HostResponse() {
		super();
	}

	
}
