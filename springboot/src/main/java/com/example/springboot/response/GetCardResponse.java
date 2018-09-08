package com.example.springboot.response;

import java.util.List;

import com.example.springboot.model.card.Card;

public class GetCardResponse extends CardResponse {
	private String targetUser;

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public GetCardResponse(String userName, ResponseType responseType, String targetUser, List<Card> cards) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.targetUser = targetUser;
		this.responseType = responseType;
	}

	public GetCardResponse() {
		super();
	}

}
