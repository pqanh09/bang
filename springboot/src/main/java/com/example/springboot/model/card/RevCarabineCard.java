package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class RevCarabineCard extends Card {

	public RevCarabineCard() {
		super();
	}

	public RevCarabineCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.gun;
		this.description = "";
		this.name = "REVCARABINE";
	}

	@Override
	public void run(Character character) {
		character.setGun(4);
	}

}
