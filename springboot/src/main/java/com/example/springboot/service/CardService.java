package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.BeerCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.model.card.CatPalouCard;
import com.example.springboot.model.card.DuelloCard;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.GatlingCard;
import com.example.springboot.model.card.GeneralStoreCard;
import com.example.springboot.model.card.IndiansCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.model.card.MustangCard;
import com.example.springboot.model.card.PanicCard;
import com.example.springboot.model.card.RemingtonCard;
import com.example.springboot.model.card.RevCarabineCard;
import com.example.springboot.model.card.SaloonCard;
import com.example.springboot.model.card.SchofieldCard;
import com.example.springboot.model.card.ScopeCard;
import com.example.springboot.model.card.StageCoachCard;
import com.example.springboot.model.card.VolcanicCard;
import com.example.springboot.model.card.WellsFargoCard;
import com.example.springboot.model.card.WinchesterCard;

@Service("cardService")
public class CardService {
	
	private static final Logger logger = LoggerFactory.getLogger(CardService.class);
	private List<Card> cards = new ArrayList<>();

	public CardService() {
		init();
	}

	private void init() {
	
		cards.add(new BangCard(Suit.diamonds, 7, "7R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 8, "8R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 9, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 10, "10R-Bang.jpg"));

		
		cards.add(new BangCard(Suit.spades, 14, "14B-Bang.jpg"));
		//BarrelCard
		cards.add(new BarrelCard(Suit.hearts, 12, "12C-Barrel.jpg"));
		cards.add(new BarrelCard(Suit.spades, 13, "13B-Barrel.jpg"));
		//
		//BeerCard

		cards.add(new BeerCard(Suit.hearts, 9, "9C-Beer.jpg"));
		cards.add(new BeerCard(Suit.hearts, 10, "10C-Beer.jpg"));
		cards.add(new BeerCard(Suit.hearts, 11, "11C-Beer.jpg"));
		//CatPalouCard
		cards.add(new CatPalouCard(Suit.hearts, 13, "13C-CatPalou.jpg"));
		cards.add(new CatPalouCard(Suit.diamonds, 9, "9R-CatPalou.jpg"));
		cards.add(new CatPalouCard(Suit.diamonds, 10, "10R-CatPalou.jpg"));
		cards.add(new CatPalouCard(Suit.diamonds, 11, "11R-CatPalou.jpg"));
		//DuelloCard
		cards.add(new DuelloCard(Suit.clubs, 9, "8Ch-Duello.jpg"));
		cards.add(new DuelloCard(Suit.spades, 10, "11B-Duello.jpg"));
		cards.add(new BangCard(Suit.diamonds, 4, "4R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 5, "5R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 6, "6R-Bang.jpg"));
		cards.add(new DuelloCard(Suit.diamonds, 11, "12R-Duello.jpg"));
		//DynamiteCard
		cards.add(new DynamiteCard(Suit.hearts, 2, "2C-Dynamite.jpg"));
		cards.add(new BeerCard(Suit.hearts, 6, "6C-Beer.jpg"));
		cards.add(new BeerCard(Suit.hearts, 7, "7C-Beer.jpg"));
		cards.add(new BeerCard(Suit.hearts, 8, "8C-Beer.jpg"));
		
		
		//GatlingCard
		cards.add(new GatlingCard(Suit.hearts, 10, "10C-Gatling.jpg"));
//		GeneralStoreCard
		cards.add(new GeneralStoreCard(Suit.spades, 12, "12B-GeneralStore.jpg"));
		cards.add(new GeneralStoreCard(Suit.clubs, 9, "9Ch-GeneralStore.jpg"));
//		IndiansCard
		cards.add(new IndiansCard(Suit.diamonds, 13, "13R-Indians.jpg"));
		cards.add(new BangCard(Suit.hearts, 12, "12C-Bang.jpg"));
		cards.add(new BangCard(Suit.hearts, 13, "13C-Bang.jpg"));
		cards.add(new BangCard(Suit.hearts, 14, "14C-Bang.jpg"));
		cards.add(new IndiansCard(Suit.diamonds, 14, "14R-Indians.jpg"));
		//JailCard
		cards.add(new JailCard(Suit.hearts, 4, "4C-Jail.jpg"));
		cards.add(new BangCard(Suit.clubs, 2, "2Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 3, "3Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 4, "4Ch-Bang.jpg"));
		cards.add(new JailCard(Suit.spades, 10, "10B-Jail.jpg"));
		cards.add(new JailCard(Suit.spades, 11, "11B-Jail.jpg"));
		//MissedCard



		cards.add(new MissedCard(Suit.clubs, 13, "13Ch-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 14, "14Ch-Missed.jpg"));
		//MustangCard
		cards.add(new MustangCard(Suit.hearts, 8, "8C-Mustang.jpg"));
		cards.add(new MustangCard(Suit.hearts, 9, "9C-Mustang.jpg"));
		//PanicCard
		cards.add(new PanicCard(Suit.hearts, 11, "11C-Panic.jpg"));
		cards.add(new BangCard(Suit.clubs, 8, "8Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 9, "9Ch-Bang.jpg"));
		
		cards.add(new BangCard(Suit.diamonds, 2, "2R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 3, "3R-Bang.jpg"));
		cards.add(new PanicCard(Suit.hearts, 12, "12C-Panic.jpg"));
		cards.add(new PanicCard(Suit.hearts, 14, "14C-Panic.jpg"));
		cards.add(new MissedCard(Suit.spades, 2, "2B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 3, "3B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 4, "4B-Missed.jpg"));
		cards.add(new PanicCard(Suit.diamonds, 8, "8R-Panic.jpg"));
		//RemingtonCard
		cards.add(new RemingtonCard(Suit.clubs, 13, "13Ch-Remington.jpg"));
		//RevCarabineCard
		cards.add(new RevCarabineCard(Suit.clubs, 14, "14Ch-RevCarabine.jpg"));
		//SaloonCard
		cards.add(new SaloonCard(Suit.hearts, 5, "5C-Saloon.jpg"));
		//SchofieldCard
		cards.add(new SchofieldCard(Suit.clubs, 11, "11Ch-Schofield.jpg"));
		cards.add(new MissedCard(Suit.spades, 5, "5B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 6, "6B-Missed.jpg"));

		cards.add(new SchofieldCard(Suit.clubs, 12, "12Ch-Schofield.jpg"));
		cards.add(new MissedCard(Suit.spades, 7, "7B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 8, "8B-Missed.jpg"));
		cards.add(new SchofieldCard(Suit.spades, 13, "13B-Schofield.jpg"));
		//ScopeCard
		cards.add(new ScopeCard(Suit.spades, 14, "14B-Scope.jpg"));
		//StageCoachCard
		cards.add(new StageCoachCard(Suit.spades, 9, "9B-StageCoach.jpg"));
		//VolcanicCard
		cards.add(new VolcanicCard(Suit.spades, 10, "10B-Volcanic.jpg"));
		cards.add(new MissedCard(Suit.clubs, 10, "10Ch-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 11, "11Ch-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 12, "12Ch-Missed.jpg"));
		cards.add(new VolcanicCard(Suit.clubs, 10, "10Ch-Volcanic.jpg"));
//		WellsFargoCard
		cards.add(new WellsFargoCard(Suit.hearts, 3, "3C-WellsFargo.jpg"));
		cards.add(new BangCard(Suit.clubs, 5, "5Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 6, "6Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 7, "7Ch-Bang.jpg"));
		//WinchesterCard
		cards.add(new WinchesterCard(Suit.spades, 8, "8B-Winchester.jpg"));
		cards.add(new BangCard(Suit.diamonds, 11, "11R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 12, "12R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 13, "13R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 14, "14R-Bang.jpg"));
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//
//		cards.add(new GatlingCard(Suit.hearts, 20, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 21, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 22, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 23, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 24, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 25, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 26, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 27, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 28, "10C-Gatling.jpg"));
//		cards.add(new GatlingCard(Suit.hearts, 29, "10C-Gatling.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 20, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 21, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 22, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 23, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 24, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 25, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 26, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 27, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 28, "13B-Barrel.jpg"));
//		cards.add(new BarrelCard(Suit.spades, 29, "13B-Barrel.jpg"));
		
		cards.add(new BangCard(Suit.diamonds, 20, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 21, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 22, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 23, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 24, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 25, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 26, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 27, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 28, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 28, "9R-Bang.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 20, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 21, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 22, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 23, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 24, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 25, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 26, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 27, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 28, "12Ch-Missed.jpg"));
//		cards.add(new MissedCard(Suit.clubs, 29, "12Ch-Missed.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 20, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 21, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 22, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 23, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 24, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 25, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 26, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 27, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 28, "8R-Panic.jpg"));
//		cards.add(new PanicCard(Suit.diamonds, 29, "8R-Panic.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 20, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 21, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 22, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 23, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 24, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 25, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 26, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 27, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 28, "9C-Mustang.jpg"));
//		cards.add(new MustangCard(Suit.hearts, 29, "9C-Mustang.jpg"));
//		
//		cards.add(new WinchesterCard(Suit.spades, 20, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 21, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 22, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 23, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 24, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 25, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 26, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 27, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 28, "8B-Winchester.jpg"));
//		cards.add(new WinchesterCard(Suit.spades, 29, "8B-Winchester.jpg"));
		
//		cards.add(new SaloonCard(Suit.hearts, 21, "5C-Saloon.jpg"));
//		cards.add(new SaloonCard(Suit.hearts, 22, "5C-Saloon.jpg"));
//		cards.add(new SaloonCard(Suit.hearts, 23, "5C-Saloon.jpg"));
//		cards.add(new SaloonCard(Suit.hearts, 24, "5C-Saloon.jpg"));
//		cards.add(new SaloonCard(Suit.hearts, 25, "5C-Saloon.jpg"));
//		cards.add(new SaloonCard(Suit.hearts, 26, "5C-Saloon.jpg"));
//		cards.add(new SaloonCard(Suit.hearts, 27, "5C-Saloon.jpg"));
//		cards.add(new SaloonCard(Suit.hearts, 28, "5C-Saloon.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 21, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 22, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 23, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 24, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 25, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 26, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 27, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 28, "9Ch-GeneralStore.jpg"));
//		cards.add(new GeneralStoreCard(Suit.clubs, 29, "9Ch-GeneralStore.jpg"));
		
//		cards.add(new DynamiteCard(Suit.hearts, 3, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 4, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 5, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 6, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 7, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 8, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 9, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 10, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 11, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 12, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 13, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 14, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 15, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 16, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 17, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 18, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 19, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 20, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 21, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 22, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 23, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 24, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 25, "2C-Dynamite.jpg"));
//		cards.add(new DynamiteCard(Suit.hearts, 26, "2C-Dynamite.jpg"));
//		cards.add(new JailCard(Suit.spades, 21, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 22, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 23, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 24, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 25, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 31, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 32, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 33, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 43, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 55, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 66, "11B-Jail.jpg"));
//		cards.add(new JailCard(Suit.spades, 88, "11B-Jail.jpg"));
		
		Collections.shuffle(cards);Collections.shuffle(cards);Collections.shuffle(cards);
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

}
