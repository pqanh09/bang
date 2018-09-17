package com.example.springboot.response;

import java.util.List;

import com.example.springboot.model.card.Card;

public class RemoveCardResponse extends CardResponse {

	public RemoveCardResponse(String userName, List<Card> cards) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.responseType = ResponseType.RemoveCard;
	}
	public RemoveCardResponse(String userName, ResponseType responseType ,List<Card> cards) {
		super();
		this.userName = userName;
		this.cards = cards;
		this.responseType = responseType;
	}
	public RemoveCardResponse() {
		super();
	}

}
