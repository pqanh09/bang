package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class CatPalouCard extends Card{
	
	
	

	public CatPalouCard() {
		super();
	}

	public CatPalouCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "CAT PALOU";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		
		
	}

}
