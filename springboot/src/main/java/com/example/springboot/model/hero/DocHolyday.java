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

public class DocHolyday extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(DocHolyday.class);

	@Override
	public void useSkill() {
		logger.info("using DocHolyday Herro's Skill");

	}

	public DocHolyday() {
		this.name = "DocHolyday";
		this.skillDescription = "Description " + name;
		this.id = "DocHolyday";
		this.lifePoint = 4;
		this.setImage("Hero-DocHolyday.jpg");
		this.autoUseSkill = false;
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
		String sessionId = match.getUserMap().get(userName);
		TurnNode turnNode = match.getCurrentTurn();
		if(step == 1) {
			List<Card> cards = new ArrayList<>();
			cards.addAll(character.getCardsInFront());
			cards.addAll(character.getCardsInHand());
			if(cards.size() < 2 || !turnNode.isAlreadyCheckedDynamite() 
					|| !turnNode.isAlreadyCheckedJail() 
					|| !turnNode.isAlreadyGetCard()
					|| turnNode.isDocHolyday()) {
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
						new SkillResponse(userName, false));
				return false;
			}
			//auto
			turnNode.getCardTemp().clear();
			turnNode.setCardTemp(cards);
			//
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2 , null, cards, character.getHero(), null));
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownStart, userName, 15));
		} else if(step == 2) {
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
						logger.error("Error when perform DocHolyday's skill");
						continue;
					}
					cards.add(card);
				}
			}
			turnNode.getCardTemp().clear();
			turnNode.getCardTemp().addAll(cards);

			//
			
			for (Card card : cards) {
				character.getCardsInFront().remove(card);
				character.getCardsInHand().remove(card);
			}
			character.setNumCardsInHand(character.getCardsInHand().size());
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			List<String> otherPlayers = new ArrayList<>(BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName));
			//auto
			turnNode.getTemp().clear();
			turnNode.setTemp(otherPlayers);
			//
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 3 , otherPlayers, null, character.getHero(), null));
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownStart, userName, 15));
		} else {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownEnd, userName, 20));
			String targetPlayer =  (String) others.get("targetUser");
			// auto
			if(StringUtils.isBlank(targetPlayer)) {
				List<String> otherPlayers = turnNode.getTemp();
				targetPlayer = otherPlayers.get(new Random().nextInt(otherPlayers.size()));
			}
			
			String message = "Removed cards:";
			String serverMessage = "- Using " + character.getHero().getName() + "'skill to bang " + targetPlayer;
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
					new HeroSkillResponse(userName, turnNode.getCardTemp(), message, serverMessage, character.getHero()));
			//
			turnNode.getTemp().clear();
			turnNode.getCardTemp().clear();
			//
			
			turnNode.setAction(ResponseType.Bang);
			turnNode.setDocHolyday(true);
			turnNode.getNextPlayer().clear();
			turnNode.getNextPlayer().add(targetPlayer);
			turnNode.requestOtherPlayerUseCard(match);
		}
		return true;
	}

}
