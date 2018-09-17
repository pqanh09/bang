package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class KitCarlson extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(KitCarlson.class);

	@Override
	public void useSkill() {

	}

	public KitCarlson() {
		this.name = "KitCarlson";
		this.skillDescription = "Description " + name;
		this.id = "KitCarlson";
		this.lifePoint = 4;
		this.setImage("Hero-KitCarlson.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step, Map<String, Object> others) {
		String userName = character.getUserName();
		String sessionId = match.getUserMap().get(userName);
		if(step == 1) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<Card> cards = commonService.getFromNewCardList(match, 3);
			character.getCardsInHand().addAll(cards);
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(true, 2, null, cards, character.getHero()));
		} else {
			Entry<String, Object> entry =  others.entrySet().iterator().next();
			Card card =  commonService.getCardInHand(character, (String) entry.getValue());
			match.getNewCards().addFirst(card);
			character.setNumCardsInHand(character.getCardsInHand().size());

			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), character, sessionId);
			
			match.getCurrentTurn().setAlreadyGetCard(true);
			
			match.getCurrentTurn().run(match);
		}
		return true;
	}


}
