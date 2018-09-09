package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class MissedCard extends Card{
	
	
	public MissedCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MissedCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.physical;
		this.description = "Missed!";
		this.name = "MISSED";
		this.setImage(image);
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void run(Character character) {
		// TODO Auto-generated method stub
		
	}

}
