package com.example.springboot.model;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.Match.MatchStatus;

public class MatchVO {
	private String matchId;
	private List<String> players = new ArrayList<>();
	private MatchStatus status = MatchStatus.waiting;
	public MatchVO(Match match) {
		this.players.addAll(match.getUserMap().keySet());
		this.matchId = match.getMatchId();
		this.status = match.getStatus();
	}
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	public List<String> getPlayers() {
		return players;
	}
	public void setPlayers(List<String> players) {
		this.players = players;
	}
	public MatchStatus getStatus() {
		return status;
	}
	public void setStatus(MatchStatus status) {
		this.status = status;
	}
	
}
