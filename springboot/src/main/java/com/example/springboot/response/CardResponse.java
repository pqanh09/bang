package com.example.springboot.response;

import java.util.List;

import com.example.springboot.model.card.Card;

public class CardResponse extends UserResponse{
	protected List<Card> cards;
	

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public CardResponse(ResponseType responseType, String userName, List<Card> cards, int countDown) {
		this.responseType = responseType;
		this.userName = userName;
		this.cards = cards;
		this.countDown = countDown;
	}
	public CardResponse(ResponseType responseType, String userName, int countDown) {
		this.responseType = responseType;
		this.userName = userName;
		this.countDown = countDown;
	}
	
	public CardResponse() {
		super();
	}

	

	
}
