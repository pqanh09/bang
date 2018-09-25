package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.card.Card;

public class UseCardInTurnResponse extends UserResponse {
	private List<String> targetUsers = new ArrayList<>();
	private Card card;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public List<String> getTargetUsers() {
		return targetUsers;
	}

	public void setTargetUsers(List<String> targetUsers) {
		this.targetUsers = targetUsers;
	}

	public UseCardInTurnResponse(String userName, Card card) {
		super();
		this.userName = userName;
		this.card = card;
		this.responseType = ResponseType.UseCardInTurn;
	}
	public UseCardInTurnResponse(String userName, Card card, String targetUser) {
		super();
		this.userName = userName;
		this.card = card;
		this.targetUsers.add(targetUser);
		this.responseType = ResponseType.UseCardInTurn;
	}
	public UseCardInTurnResponse(String userName, Card card, List<String> targetUsers) {
		super();
		this.userName = userName;
		this.card = card;
		this.targetUsers = targetUsers;
		this.responseType = ResponseType.UseCardInTurn;
	}

	public UseCardInTurnResponse() {
		super();
	}

}
