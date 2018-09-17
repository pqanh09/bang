package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class GeneralStoreCard extends Card{
	
	
	

	public GeneralStoreCard() {
		super();
	}

	public GeneralStoreCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "GENERAL STORE";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		
		
	}

}
