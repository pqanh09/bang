package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class PanicCard extends Card{
	
	
	

	public PanicCard() {
		super();
	}

	public PanicCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "PANIC";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
