package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class DynamiteCard extends Card {

	public DynamiteCard() {
		super();
	}

	public DynamiteCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "DYNAMITE";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		character.setHasDynamite(true);
	}

	@Override
	public void remove(Character character) {
		character.setHasDynamite(false);
	}

}
