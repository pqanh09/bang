package com.example.springboot.response;

import java.util.List;

import com.example.springboot.model.card.Card;

public class GetCardResponse extends CardResponse {
	private String targetUser;
	private boolean hasCardInHand = false;

	
	public boolean isHasCardInHand() {
		return hasCardInHand;
	}

	public void setHasCardInHand(boolean hasCardInHand) {
		this.hasCardInHand = hasCardInHand;
	}

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public GetCardResponse(String userName, ResponseType responseType, String targetUser, List<Card> cards, boolean hasCardInHand, int countDown) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.targetUser = targetUser;
		this.responseType = responseType;
		this.hasCardInHand = hasCardInHand;
		this.countDown = countDown;
	}

	public GetCardResponse() {
		super();
	}

}
