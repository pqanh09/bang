package com.example.springboot.response;

import java.util.HashMap;
import java.util.Map;

import com.example.springboot.model.CharacterVO;

public class HostResponse extends UserResponse{
	private String matchId;
	private boolean host = false;
	private Map<Integer, CharacterVO> mapCharacter = new HashMap<>();

	
	public Map<Integer, CharacterVO> getMapCharacter() {
		return mapCharacter;
	}

	public void setMapCharacter(Map<Integer, CharacterVO> mapCharacter) {
		this.mapCharacter = mapCharacter;
	}

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

	public HostResponse(ResponseType responseType, String userName, String matchId, Map<Integer, CharacterVO> mapCharacter) {
		super(responseType, userName);
		this.matchId = matchId;
		this.mapCharacter = mapCharacter;
	}
	public HostResponse(ResponseType responseType, String userName, boolean host) {
		super(responseType, userName);
		this.host = host;
	}

	public HostResponse() {
		super();
	}

	
}
