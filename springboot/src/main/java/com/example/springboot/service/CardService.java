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
		int n = 0;
		// ok
		for (int i = 1; i <= 20; i++) {
			n++;
			cards.add(new BangCard(Suit.diamonds, String.valueOf(n)));
		}
		
		//
		for (int i = 1; i <= 20; i++) {
			n++;
			cards.add(new BarrelCard(Suit.hearts, String.valueOf(n)));
		}
		
		//
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new BeerCard(Suit.clubs, String.valueOf(n)));
		}
		
		// ok
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new CatPalouCard(Suit.spades, String.valueOf(n)));
		}
		
		//
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new DuelloCard(Suit.clubs, String.valueOf(n)));
		}
		
		// ok
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new GatlingCard(Suit.diamonds, String.valueOf(n)));
		}
		
		// ok
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new GeneralStoreCard(Suit.hearts, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new IndiansCard(Suit.spades, String.valueOf(n)));
		}
		
		// ok
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new MissedCard(Suit.clubs, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new MustangCard(Suit.diamonds, String.valueOf(n)));
		}
		
		// ok
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new PanicCard(Suit.spades, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new RemingtonCard(Suit.clubs, String.valueOf(n)));
		}
		
		// ok 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new RevCarabineCard(Suit.diamonds, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new SaloonCard(Suit.hearts, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new SchofieldCard(Suit.spades, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new ScopeCard(Suit.clubs, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new StageCoachCard(Suit.diamonds, String.valueOf(n)));
		}
		
		// ok
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new VolcanicCard(Suit.hearts, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new WellsFargoCard(Suit.spades, String.valueOf(n)));
		}
		
		// 
		for (int i = 1; i <= 5; i++) {
			n++;
			cards.add(new WinchesterCard(Suit.clubs, String.valueOf(n)));
		}
		// 
		for (int i = 1; i <= 20; i++) {
			n++;
			cards.add(new JailCard(Suit.diamonds, String.valueOf(n)));
		}
		// 
		for (int i = 1; i <= 20; i++) {
			n++;
			cards.add(new DynamiteCard(Suit.hearts, String.valueOf(n)));
		}
		
		
		
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
		
		
		
		
		Collections.shuffle(cards);
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

}
