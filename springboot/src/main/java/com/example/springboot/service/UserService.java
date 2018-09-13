package com.example.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {
	private Map<String, String> userMap = new HashMap<>();
	private Map<String, String> sessionIdMap = new HashMap<>();
	
	public UserService() {
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	public Map<String, String> getSessionIdMap() {
		return sessionIdMap;
	}

	public void setSessionIdMap(Map<String, String> sessionIdMap) {
		this.sessionIdMap = sessionIdMap;
	}

	
}
