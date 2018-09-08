package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class GeneralStoreCard extends Card{
	
	
	

	public GeneralStoreCard() {
		super();
	}

	public GeneralStoreCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "GENERAL STORE";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
