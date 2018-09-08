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

	public CardResponse(ResponseType responseType, String userName, List<Card> cards) {
		this.responseType = responseType;
		this.userName = userName;
		this.cards = cards;
	}
	public CardResponse(ResponseType responseType, String userName) {
		this.responseType = responseType;
		this.userName = userName;
	}
	
	public CardResponse() {
		super();
	}

	

	
}
