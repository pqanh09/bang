package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class DuelloCard extends Card{
	
	
	

	public DuelloCard() {
		super();
	}

	public DuelloCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "DUELLO";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
