package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class RemingtonCard extends Card {

	public RemingtonCard() {
		super();
	}

	public RemingtonCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.gun;
		this.description = "";
		this.name = "REMINGTON";
		this.setImage(image);
		this.number = number;
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		character.setGun(3);
	}

	@Override
	public void remove(Character character) {
		character.setGun(1);
	}
}
