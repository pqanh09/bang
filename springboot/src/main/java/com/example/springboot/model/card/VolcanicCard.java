package com.example.springboot.model.card;

import com.example.springboot.model.Character;

public class VolcanicCard extends Card{
	
	
	

	public VolcanicCard() {
		super();
	}

	public VolcanicCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.gun;
		this.description = "You can play any number of BANG!";
		this.name = "VOLCANIC";
	}

	@Override
	public void run(Character character) {
		character.setGun(1);
		
	}

}
