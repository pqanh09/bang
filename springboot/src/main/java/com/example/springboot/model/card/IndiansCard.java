package com.example.springboot.model.card;
import com.example.springboot.model.Character;
public class IndiansCard extends Card{
	
	
	

	public IndiansCard() {
		super();
	}

	public IndiansCard(Suit suit, String id) {
		super(suit, id);
		this.cardType = CardType.magic;
		this.description = "All other players discard a BANG! or lose 1 life point.";
		this.name = "INDIANS";
	}

	@Override
	public void run(Character character) {
		
		
	}

}
