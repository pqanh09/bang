package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class ScopeCard extends Card{
	
	
	

	public ScopeCard() {
		super();
	}

	public ScopeCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.viewothers;
		this.description = "";
		this.name = "SCOPE";
	}

	@Override
	public void run(Character character) {
		character.setViewOthers(1);
	}

}
