package com.example.springboot.response;

	public class UserResponse extends Response{
	protected String userName;
	protected int countDown = 10;
	
	public int getCountDown() {
		return countDown;
	}

	public void setCountDown(int countDown) {
		this.countDown = countDown;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserResponse(ResponseType responseType, String userName, int countDown) {
		super(responseType);
		this.userName = userName;
		this.countDown = countDown;
	}
	
	public UserResponse(ResponseType responseType, String userName) {
		super(responseType);
		this.userName = userName;
//		this.countDown = countDown;
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
