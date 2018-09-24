package com.example.springboot.model.hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class SidKetchum extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(SidKetchum.class);

	@Override
	public void useSkill() {

	}

	public SidKetchum() {
		this.name = "SidKetchum";
		this.skillDescription = "Description " + name;
		this.id = "SidKetchum";
		this.lifePoint = 4;
		this.setImage("Hero-SidKetchum.jpg");
		this.autoUseSkill = false;
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step, Map<String, Object> others) {
		String userName = character.getUserName();
		String sessionId = match.getUserMap().get(userName);
		TurnNode turnNode = match.getCurrentTurn();
		if(character.getLifePoint() == character.getCapacityLPoint() || !turnNode.isAlreadyCheckedDynamite() || !turnNode.isAlreadyCheckedJail() || !turnNode.isAlreadyGetCard()) {
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, false));
			return false;
		}
		if(step == 1) {
			List<Card> cards = new ArrayList<>();
			cards.addAll(character.getCardsInFront());
			cards.addAll(character.getCardsInHand());
			if(cards.size() < 2) {
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
						new SkillResponse(userName, false));
				return false;
			}
			//auto
			turnNode.getCardTemp().clear();
			turnNode.setCardTemp(cards);
			//
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2 , null, cards, character.getHero(), null));
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownStart, userName, 20));
		} else {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownEnd, userName, 20));
			List<String> cardIds =  (List<String>) others.get("cards");
			List<Card> cards = new ArrayList<>();
			//auto
			if(cardIds == null || cardIds.isEmpty() || cardIds.size() != 2) {
				List<Card> cardTemps = turnNode.getCardTemp();
				Collections.shuffle(cardTemps);
				cards.add(cardTemps.get(0));
				cards.add(cardTemps.get(1));
			} else {
				for (String cardId : cardIds) {
					Card  card = BangUtils.findCardInFront(character, cardId);
					if(card == null)  {
						card = BangUtils.findCardInHand(character, cardId);
					}
					if(card == null) {
						logger.error("Error when perform SidKetchum's skill");
						continue;
					}
					cards.add(card);
				}
			}
			turnNode.getCardTemp().clear();
			//
			
			for (Card card : cards) {
				character.getCardsInFront().remove(card);
				character.getCardsInHand().remove(card);
				commonService.addToOldCardList(card, match);
			}
			character.setLifePoint(character.getLifePoint() + 1);
			character.setNumCardsInHand(character.getCardsInHand().size());
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
		}
		return true;
	}


}
