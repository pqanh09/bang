package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class MissedCard extends Card{
	
	
	public MissedCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MissedCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.physical;
		this.description = "Missed!";
		this.name = "MISSED";
	}

	@Override
	public void run(Character character) {
		// TODO Auto-generated method stub
		
	}

}
