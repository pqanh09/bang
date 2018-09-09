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
		
		cards.add(new BangCard(Suit.hearts, 12, "12C-Bang.jpg"));
		cards.add(new BangCard(Suit.hearts, 13, "13C-Bang.jpg"));
		cards.add(new BangCard(Suit.hearts, 14, "14C-Bang.jpg"));
		
		cards.add(new BangCard(Suit.clubs, 2, "2Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 3, "3Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 4, "4Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 5, "5Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 6, "6Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 7, "7Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 8, "8Ch-Bang.jpg"));
		cards.add(new BangCard(Suit.clubs, 9, "9Ch-Bang.jpg"));
		
		cards.add(new BangCard(Suit.diamonds, 2, "2R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 3, "3R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 4, "4R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 5, "5R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 6, "6R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 7, "7R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 8, "8R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 9, "9R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 10, "10R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 11, "11R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 12, "12R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 13, "13R-Bang.jpg"));
		cards.add(new BangCard(Suit.diamonds, 14, "14R-Bang.jpg"));
		
		cards.add(new BangCard(Suit.spades, 14, "14B-Bang.jpg"));
		//BarrelCard
		cards.add(new BarrelCard(Suit.hearts, 12, "12C-Barrel.jpg"));
		cards.add(new BarrelCard(Suit.spades, 13, "13B-Barrel.jpg"));
		//BeerCard
		cards.add(new BeerCard(Suit.hearts, 6, "6C-Beer.jpg"));
		cards.add(new BeerCard(Suit.hearts, 7, "7C-Beer.jpg"));
		cards.add(new BeerCard(Suit.hearts, 8, "8C-Beer.jpg"));
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
		cards.add(new DuelloCard(Suit.diamonds, 11, "12R-Duello.jpg"));
		//DynamiteCard
		cards.add(new DynamiteCard(Suit.hearts, 2, "2C-Dynamite.jpg"));
		//GatlingCard
		cards.add(new GatlingCard(Suit.hearts, 10, "10C-Gatling.jpg"));
		//GeneralStoreCard
		cards.add(new GeneralStoreCard(Suit.spades, 12, "12B-GeneralStore.jpg"));
		cards.add(new GeneralStoreCard(Suit.clubs, 9, "9Ch-GeneralStore.jpg"));
		//IndiansCard
		cards.add(new IndiansCard(Suit.diamonds, 13, "13R-Indians.jpg"));
		cards.add(new IndiansCard(Suit.diamonds, 14, "14R-Indians.jpg"));
		//JailCard
		cards.add(new JailCard(Suit.hearts, 4, "4C-Jail.jpg"));
		cards.add(new JailCard(Suit.spades, 10, "10B-Jail.jpg"));
		cards.add(new JailCard(Suit.spades, 11, "11B-Jail.jpg"));
		//MissedCard
		cards.add(new MissedCard(Suit.spades, 2, "2B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 3, "3B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 4, "4B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 5, "5B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 6, "6B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 7, "7B-Missed.jpg"));
		cards.add(new MissedCard(Suit.spades, 8, "8B-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 10, "10Ch-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 11, "11Ch-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 12, "12Ch-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 13, "13Ch-Missed.jpg"));
		cards.add(new MissedCard(Suit.clubs, 14, "14Ch-Missed.jpg"));
		//MustangCard
		cards.add(new MustangCard(Suit.hearts, 8, "8C-Mustang.jpg"));
		cards.add(new MustangCard(Suit.hearts, 9, "9C-Mustang.jpg"));
		//PanicCard
		cards.add(new PanicCard(Suit.hearts, 11, "11C-Panic.jpg"));
		cards.add(new PanicCard(Suit.hearts, 12, "12C-Panic.jpg"));
		cards.add(new PanicCard(Suit.hearts, 14, "14C-Panic.jpg"));
		cards.add(new PanicCard(Suit.diamonds, 8, "8R-Panic.jpg"));
		//RemingtonCard
		cards.add(new RemingtonCard(Suit.clubs, 13, "13Ch-Remington.jpg"));
		//RevCarabineCard
		cards.add(new RevCarabineCard(Suit.clubs, 14, "14Ch-RevCarabine.jpg"));
		//SaloonCard
		cards.add(new SaloonCard(Suit.hearts, 5, "5C-Saloon.jpg"));
		//SchofieldCard
		cards.add(new SchofieldCard(Suit.clubs, 11, "11Ch-Schofield.jpg"));
		cards.add(new SchofieldCard(Suit.clubs, 12, "12Ch-Schofield.jpg"));
		cards.add(new SchofieldCard(Suit.spades, 13, "13B-Schofield.jpg"));
		//ScopeCard
		cards.add(new ScopeCard(Suit.spades, 14, "14B-Scope.jpg"));
		//StageCoachCard
		cards.add(new StageCoachCard(Suit.spades, 9, "9B-StageCoach.jpg"));
		//VolcanicCard
		cards.add(new VolcanicCard(Suit.spades, 10, "10B-Volcanic.jpg"));
		cards.add(new VolcanicCard(Suit.clubs, 10, "10Ch-Volcanic.jpg"));
		//WellsFargoCard
		cards.add(new WellsFargoCard(Suit.hearts, 3, "3C-WellsFargo.jpg"));
		//WinchesterCard
		cards.add(new WinchesterCard(Suit.spades, 8, "8B-Winchester.jpg"));
//		
//		
//		int n = 0;
//
//
//		
//		
//		// ok
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new CatPalouCard(Suit.spades, String.valueOf(n)));
//		}
//		
//		//
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new DuelloCard(Suit.clubs, String.valueOf(n)));
//		}
//		
//		// ok
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new GatlingCard(Suit.diamonds, String.valueOf(n)));
//		}
//		
//		// ok
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new GeneralStoreCard(Suit.hearts, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new IndiansCard(Suit.spades, String.valueOf(n)));
//		}
//		
//		// ok
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new MissedCard(Suit.clubs, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new MustangCard(Suit.diamonds, String.valueOf(n)));
//		}
//		
//		// ok
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new PanicCard(Suit.spades, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new RemingtonCard(Suit.clubs, String.valueOf(n)));
//		}
//		
//		// ok 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new RevCarabineCard(Suit.diamonds, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new SaloonCard(Suit.hearts, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new SchofieldCard(Suit.spades, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new ScopeCard(Suit.clubs, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new StageCoachCard(Suit.diamonds, String.valueOf(n)));
//		}
//		
//		// ok
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new VolcanicCard(Suit.hearts, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new WellsFargoCard(Suit.spades, String.valueOf(n)));
//		}
//		
//		// 
//		for (int i = 1; i <= 5; i++) {
//			n++;
//			cards.add(new WinchesterCard(Suit.clubs, String.valueOf(n)));
//		}
//		// 
//		for (int i = 1; i <= 20; i++) {
//			n++;
//			cards.add(new JailCard(Suit.diamonds, String.valueOf(n)));
//		}
//		// 
//		for (int i = 1; i <= 20; i++) {
//			n++;
//			cards.add(new DynamiteCard(Suit.hearts, String.valueOf(n)));
//		}
//		
		
		
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
//			//System User
//			File systemUserFile = ResourceUtils.getFile("classpath:cards.properties");
//			
//			systemUser = mapper.readValue(systemUserFile, User.class);
//			//Support User
//			File supportUserFile = ResourceUtils.getFile("classpath:userManagement/user/SupportUser.txt");
//			supportUser = mapper.readValue(supportUserFile, User.class);
//			
//			//System Role
//			File systemRoleFile = ResourceUtils.getFile("classpath:userManagement/role/SystemRole.txt");
//			systemRole = mapper.readValue(systemRoleFile, UserRole.class);
//			//Support Role
//			File supportRoleFile = ResourceUtils.getFile("classpath:userManagement/role/SupportRole.txt");
//			supportRole = mapper.readValue(supportRoleFile, UserRole.class);
//			
//			//System User
//			File systemGroupFile = ResourceUtils.getFile("classpath:userManagement/group/SystemGroup.txt");
//			systemGroup = mapper.readValue(systemGroupFile, Group.class);
//			//Support User
//			File supportGroupFile = ResourceUtils.getFile("classpath:userManagement/group/SupportGroup.txt");
//			supportGroup = mapper.readValue(supportGroupFile, Group.class);
			
//		} catch (Exception e) {
//			logger.error("An error when loading User, Group and Role default: ", e);
//		}
		
		
		
		
		Collections.shuffle(cards);Collections.shuffle(cards);Collections.shuffle(cards);
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

}
