package com.example.springboot.model.card;
import com.example.springboot.model.Character;
import com.example.springboot.model.hero.RoseDoolan;
public class ScopeCard extends Card{
	
	
	

	public ScopeCard() {
		super();
	}

	public ScopeCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.viewothers;
		this.description = "";
		this.name = "SCOPE";
		this.setImage(image);
		this.number = number;
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		if(character.getHero() instanceof RoseDoolan) {
			character.setViewOthers(2);
		} else {
			character.setViewOthers(1);
		}
	}
	@Override
	public void remove(Character character) {
		if(character.getHero() instanceof RoseDoolan) {
			character.setViewOthers(1);
		} else {
			character.setViewOthers(0);
		}
	}

}
