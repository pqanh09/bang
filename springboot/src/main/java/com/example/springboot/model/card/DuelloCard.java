package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class DuelloCard extends Card{
	
	
	

	public DuelloCard() {
		super();
	}

	public DuelloCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "DUELLO";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		
		
	}

}
