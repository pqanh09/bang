package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class DynamiteCard extends Card{
	
	
	

	public DynamiteCard() {
		super();
	}

	public DynamiteCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "DYNAMITE";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
