package com.example.springboot.response;

public class Response {
	protected ResponseType responseType = ResponseType.Unknown;
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
	
}
