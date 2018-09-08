package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class GatlingCard extends Card{
	
	
	

	public GatlingCard() {
		super();
	}

	public GatlingCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "";
		this.name = "GATLING";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
