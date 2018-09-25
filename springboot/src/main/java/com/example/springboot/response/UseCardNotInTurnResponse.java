package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.card.Card;

public class UseCardNotInTurnResponse extends UserResponse {
	private String targetUser;
	private List<Card> cards = new ArrayList<>();


	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public List<Card> getCards() {
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	public UseCardNotInTurnResponse(String userName, Card card) {
		super();
		this.userName = userName;
		this.cards.add(card);
		this.responseType = ResponseType.UseCardNotInTurn;
	}
	public UseCardNotInTurnResponse(String userName, List<Card> cards) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.responseType = ResponseType.UseCardNotInTurn;
	}
	public UseCardNotInTurnResponse(String userName, Card card, String targetUser) {
		super();
		this.userName = userName;
		this.cards.add(card);
		this.targetUser = targetUser;
		this.responseType = ResponseType.UseCardNotInTurn;
	}
	public UseCardNotInTurnResponse() {
		super();
	}

}
