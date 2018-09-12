package com.example.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.springboot.model.Match;
import com.example.springboot.model.MatchVO;

@Service("matchService")
public class MatchService {
	private Map<String, String> userMap = new HashMap<>();
	private Map<String, Match> matchMap = new HashMap<>();
	
	public MatchService() {
	}

	public Map<String, Match> getMatchMap() {
		return matchMap;
	}

	public void setMatchMap(Map<String, Match> matchMap) {
		this.matchMap = matchMap;
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	
}
