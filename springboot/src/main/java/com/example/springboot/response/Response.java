package com.example.springboot.response;

public class Response {
	protected ResponseType responseType = ResponseType.Unknown;
	protected String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ResponseType getResponseType() {
		return responseType;
	}
	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}
	public Response() {
		super();
	}
	public Response(ResponseType responseType) {
		super();
		this.responseType = responseType;
	}
	public Response(ResponseType responseType, String message) {
		super();
		this.responseType = responseType;
		this.message = message;
	}
	
	
}
