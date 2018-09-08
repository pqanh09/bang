package com.example.springboot.model.card;

import com.example.springboot.model.Character;
public class BangCard extends Card{
	
	public BangCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BangCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.physical;
		this.description = "Bang!";
		this.name = "BANG";
	}

	@Override
	public void run(Character character) {
		// TODO Auto-generated method stub
		
	}

}
