package com.example.springboot.response;

import java.util.List;

import com.example.springboot.model.card.Card;

public class RemoveCardResponse extends CardResponse {
	private int numberCard = 0;
	
	public int getNumberCard() {
		return numberCard;
	}
	public void setNumberCard(int numberCard) {
		this.numberCard = numberCard;
	}
	public RemoveCardResponse(String userName, ResponseType responseType ,List<Card> cards, int numberCard, int countDown) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.responseType = responseType;
		this.numberCard = numberCard;
		this.countDown = countDown;
	}
	public RemoveCardResponse() {
		super();
	}

}
