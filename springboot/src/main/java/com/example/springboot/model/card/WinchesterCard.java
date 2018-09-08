package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class WinchesterCard extends Card {

	public WinchesterCard() {
		super();
	}

	public WinchesterCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.gun;
		this.description = "";
		this.name = "WINCHESTER";
	}

	@Override
	public void run(Character character) {
		character.setGun(5);
	}

}
