package com.example.springboot.model.card;
import com.example.springboot.model.Character;
import com.example.springboot.model.hero.PaulRegret;
public class MustangCard extends Card{

	public MustangCard() {
		super();
	}

	public MustangCard(Suit suit, int number, String image) {
		super(suit, number);
		this.cardType = CardType.otherviews;
		this.description = "";
		this.name = "MUSTANG";
		this.setImage(image);
		this.number = number;
		this.id = name + suit.toString() + String.valueOf(number);
	}

	@Override
	public void apply(Character character) {
		if(character.getHero() instanceof PaulRegret) {
			character.setOthersView(2);
		} else {
			character.setOthersView(1);
		}
	}
	@Override
	public void remove(Character character) {
		if(character.getHero() instanceof PaulRegret) {
			character.setOthersView(1);
		} else {
			character.setOthersView(0);
		}
	}

}
