package com.example.springboot.response;

import com.example.springboot.model.card.Card;

public class DrawResponse extends UserResponse{
	private Card card;

	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public DrawResponse(ResponseType responseType, String userName, Card card, int countDown) {
		super(responseType, userName);
		this.card = card;
		this.countDown = countDown;
	}

	
}
