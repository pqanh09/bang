package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class RemingtonCard extends Card {

	public RemingtonCard() {
		super();
	}

	public RemingtonCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.gun;
		this.description = "";
		this.name = "REMINGTON";
	}

	@Override
	public void run(Character character) {
		character.setGun(3);
	}

}
