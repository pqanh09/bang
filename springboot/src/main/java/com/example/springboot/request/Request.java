package com.example.springboot.request;

import java.util.ArrayList;
import java.util.List;

public class Request {
	private String user;
	private String targetUser;
	private RequestType actionType;
	private String id;
	private boolean noneResponse = false;
	
	
	private List<String> ids = new ArrayList<>();

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	public boolean isNoneResponse() {
		return noneResponse;
	}

	public void setNoneResponse(boolean noneResponse) {
		this.noneResponse = noneResponse;
	}
	public RequestType getActionType() {
		return actionType;
	}
	public void setActionType(RequestType actionType) {
		this.actionType = actionType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public Request() {
		super();
	}
	
	
}
