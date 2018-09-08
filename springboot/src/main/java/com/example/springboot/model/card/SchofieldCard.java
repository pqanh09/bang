package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class SchofieldCard extends Card{
	
	
	

	public SchofieldCard() {
		super();
	}

	public SchofieldCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.gun;
		this.description = "";
		this.name = "SCHOFIELD";
	}

	@Override
	public void run(Character character) {
		character.setGun(2);
	}

}
