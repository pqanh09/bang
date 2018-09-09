package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class WellsFargoCard extends Card{
	
	
	

	public WellsFargoCard() {
		super();
	}

	public WellsFargoCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "WELLS FARGO";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		
		
	}

}
