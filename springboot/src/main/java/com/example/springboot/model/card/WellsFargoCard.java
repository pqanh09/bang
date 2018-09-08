package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class WellsFargoCard extends Card{
	
	
	

	public WellsFargoCard() {
		super();
	}

	public WellsFargoCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "WELLS FARGO";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
