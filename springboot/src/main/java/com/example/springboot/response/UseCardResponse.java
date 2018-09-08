package com.example.springboot.response;

import com.example.springboot.model.card.Card;

public class UseCardResponse extends UserResponse {
	private String targetUser;
	private Card card;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}


	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public UseCardResponse(String userName, Card card, String targetUser) {
		super();
		this.userName = userName;
		this.card = card;
		this.targetUser = targetUser;
		this.responseType = ResponseType.UseCard;
	}
	public UseCardResponse(String userName, ResponseType responseType, Card card, String targetUser) {
		super();
		this.userName = userName;
		this.card = card;
		this.targetUser = targetUser;
		this.responseType = responseType;
	}

	public UseCardResponse() {
		super();
	}

}
