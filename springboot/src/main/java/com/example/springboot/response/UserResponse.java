package com.example.springboot.response;

	public class UserResponse extends Response{
	protected String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserResponse(ResponseType responseType, String userName) {
		super(responseType);
		this.userName = userName;
	}
	public UserResponse(ResponseType responseType, String userName, String message) {
		super(responseType);
		this.userName = userName;
		this.message = message;
	}

	public UserResponse() {
		super();
	}

	
}
