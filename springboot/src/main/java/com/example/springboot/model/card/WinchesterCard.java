package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class WinchesterCard extends Card {

	public WinchesterCard() {
		super();
	}

	public WinchesterCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.gun;
		this.description = "";
		this.name = "WINCHESTER";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		character.setGun(5);
	}

}
