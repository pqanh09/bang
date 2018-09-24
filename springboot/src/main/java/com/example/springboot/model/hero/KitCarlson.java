package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.command.DynamiteActionCmd;
import com.example.springboot.command.JailActionCmd;
import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.response.UserResponse;
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
		TurnNode turnNode = match.getCurrentTurn();
		if(!turnNode.isAlreadyCheckedDynamite() || !turnNode.isAlreadyCheckedJail() || turnNode.isAlreadyGetCard()) {
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, false));
			return false;
		}
		if(step == 1) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<Card> cards = commonService.getFromNewCardList(match, 3);
			turnNode.getCardTemp().clear();
			turnNode.getCardTemp().addAll(cards);
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2, null, cards, character.getHero(), null));
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownStart, userName, 10));
		} else {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownEnd, userName, 20));
			List<String> cardIds =  (List<String>) others.get("cards");
			Card card = null;
			
			
			if(cardIds != null && !cardIds.isEmpty() && cardIds.size() == 1) {
				for (Card cd : turnNode.getCardTemp()) {
					if(cd.getId().equals(cardIds.get(0))) {
						card = cd;
						break;
					} else {
						commonService.addToOldCardList(cd, match);
					}
				}
			}
			
			if(card == null) {
				if(cardIds != null && !cardIds.isEmpty() && cardIds.size() == 1) {
					logger.error("ERROR in KitCarlson: not found {}", cardIds);
				}
				List<Card> cards = turnNode.getCardTemp();
				card = cards.get(new Random().nextInt(cards.size()));
			}
			
			turnNode.getCardTemp().remove(card);
			character.getCardsInHand().addAll(turnNode.getCardTemp());
			character.setNumCardsInHand(character.getCardsInHand().size());
			turnNode.getCardTemp().clear();
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			
			match.getNewCards().addFirst(card);
			turnNode.setAlreadyGetCard(true);
			
			turnNode.run(match);
			
		}
		return true;
	}


}
