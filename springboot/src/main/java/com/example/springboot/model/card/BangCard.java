package com.example.springboot.model.card;

import com.example.springboot.model.Character;
public class BangCard extends Card{
	
	public BangCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BangCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.physical;
		this.description = "Bang!";
		this.name = "BANG";
		this.setImage(image);
		this.number = number;
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		// TODO Auto-generated method stub
		
	}

}
