package com.example.springboot.response;

import java.util.List;

import com.example.springboot.model.card.Card;

public class OldCardResponse extends CardResponse {

	public OldCardResponse(List<Card> cards) {
		super();
		this.cards = cards;
		this.responseType = ResponseType.OldCard;
	}
	public OldCardResponse(Card card) {
		super();
		this.cards.add(card);
		this.responseType = ResponseType.OldCard;
	}
	public OldCardResponse() {
		super();
		this.responseType = ResponseType.OldCard;
	}
	

}
