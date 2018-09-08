package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class JailCard extends Card{
	
	
	

	public JailCard() {
		super();
	}

	public JailCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "JAIL";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
