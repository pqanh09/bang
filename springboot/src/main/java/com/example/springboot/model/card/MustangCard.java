package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class MustangCard extends Card{
	
	
	

	public MustangCard() {
		super();
	}

	public MustangCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.otherviews;
		this.description = "";
		this.name = "MUSTANG";
	}

	@Override
	public void run(Character character) {
		character.setOthersView(1);
	}

}
