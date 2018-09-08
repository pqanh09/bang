package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class CatPalouCard extends Card{
	
	
	

	public CatPalouCard() {
		super();
	}

	public CatPalouCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "CAT PALOU";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
