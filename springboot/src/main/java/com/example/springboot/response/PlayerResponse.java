package com.example.springboot.response;

import java.util.HashMap;
import java.util.Map;

import com.example.springboot.model.CharacterVO;

public class PlayerResponse extends UserResponse{
	private String matchId;
	private boolean host = false;
	private Map<Integer, CharacterVO> playerMap = new HashMap<>();

	public Map<Integer, CharacterVO> getPlayerMap() {
		return playerMap;
	}

	public void setPlayerMap(Map<Integer, CharacterVO> playerMap) {
		this.playerMap = playerMap;
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

	public PlayerResponse(String userName, String matchId, Map<Integer, CharacterVO> playerMap) {
		super(ResponseType.Player, userName);
		this.matchId = matchId;
		this.playerMap = playerMap;
	}
	
	public PlayerResponse() {
		super();
	}

	
}
