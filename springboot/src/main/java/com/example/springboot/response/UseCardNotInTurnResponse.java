package com.example.springboot.response;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot.model.card.Card;

public class UseCardNotInTurnResponse extends UserResponse {
	protected String targetUser;
	protected List<Card> cards = new ArrayList<>();
	protected String serverMessage;
	
	public String getServerMessage() {
		return serverMessage;
	}
	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}
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
	public UseCardNotInTurnResponse(String userName, List<Card> cards, String message, String serverMessage) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.responseType = ResponseType.UseCardNotInTurn;
		this.message = message;
		this.serverMessage = serverMessage;
	}
	public UseCardNotInTurnResponse(String userName, Card card, String targetUser, String message, String serverMessage) {
		super();
		this.userName = userName;
		this.cards.add(card);
		this.targetUser = targetUser;
		this.responseType = ResponseType.UseCardNotInTurn;
		this.message = message;
		this.serverMessage = serverMessage;
	}
	public UseCardNotInTurnResponse(String userName, String targetUser, String message, String serverMessage) {
		super();
		this.userName = userName;
		this.targetUser = targetUser;
		this.responseType = ResponseType.UseCardNotInTurn;
		this.message = message;
		this.serverMessage = serverMessage;
	}
	
	
	
	public UseCardNotInTurnResponse(ResponseType responseType, String userName) {
		super(responseType, userName);
	}
	public UseCardNotInTurnResponse() {
		super();
	}

}
