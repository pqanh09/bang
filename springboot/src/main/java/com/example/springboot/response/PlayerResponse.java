package com.example.springboot.response;

import java.util.Map;

public class PlayerResponse extends UserResponse{
	private Map<String, Integer> playerMap;
	

	public Map<String, Integer> getPlayerMap() {
		return playerMap;
	}


	public void setPlayerMap(Map<String, Integer> playerMap) {
		this.playerMap = playerMap;
	}

	public PlayerResponse(String userName, Map<String, Integer> playerMap) {
		super(ResponseType.Player, userName);
		this.playerMap = playerMap;
	}


	public PlayerResponse() {
		super();
	}

	
}
