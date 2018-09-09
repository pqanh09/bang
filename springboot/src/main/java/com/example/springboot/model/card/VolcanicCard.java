package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class VolcanicCard extends Card{
	
	
	

	public VolcanicCard() {
		super();
	}

	public VolcanicCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.gun;
		this.description = "You can play any number of BANG!";
		this.name = "VOLCANIC";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		character.setGun(1);
		
	}

}
