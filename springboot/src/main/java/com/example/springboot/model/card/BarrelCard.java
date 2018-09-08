package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class BarrelCard extends Card{
	
	
	

	public BarrelCard() {
		super();
	}

	public BarrelCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.barrel;
		this.description = "";
		this.name = "BARREL";
	}

	@Override
	public void run(Character character) {
		character.setBarrel(true);
	}

	

}
