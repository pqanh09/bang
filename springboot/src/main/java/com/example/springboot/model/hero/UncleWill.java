package com.example.springboot.model.hero;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;
import com.rabbitmq.client.AMQP.Connection.Tune;

public class UncleWill extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(UncleWill.class);

	@Override
	public void useSkill() {
		logger.info("using UncleWill Herro's Skill");

	}

	public UncleWill() {
		this.name = "UncleWill";
		this.skillDescription = "Description " + name;
		this.id = "UncleWill";
		this.lifePoint = 4;
		this.setImage("Hero-UncleWill.jpg");
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
		if((character.getCardsInFront().isEmpty() && character.getCardsInHand().isEmpty()) 
				|| !turnNode.isAlreadyCheckedDynamite() 
				|| !turnNode.isAlreadyCheckedJail() 
				|| !turnNode.isAlreadyGetCard()
				|| turnNode.isUncleWill()) {
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, false));
			return false;
		}
		if(step == 1) {
			List<Card> cards = new ArrayList<>();
			cards.addAll(character.getCardsInFront());
			cards.addAll(character.getCardsInHand());
			if(cards.isEmpty()) {
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
						new SkillResponse(userName, false));
				return false;
			}
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2 , null, cards, character.getHero(), null));
		} else {
			List<String> cardIds =  (List<String>) others.get("cards");
			Card card =  commonService.getCardInFront(character, cardIds.get(0));
			if(card == null)  {
				card =  commonService.getCardInHand(character, cardIds.get(0));
			}
			if(card == null) {
				logger.error("Error when perform UncleWill's skill");
				return false;
			}
			commonService.addToOldCardList(card, match);

			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			
			turnNode.setUncleWill(true);
			
			turnNode.setAction(ResponseType.GeneralStore);
			turnNode.setCardTemp(commonService.getFromNewCardList(match, match.getPlayerTurnQueue().size()));
			LinkedList<String> otherPlayers = BangUtils.getOtherPlayer(match.getPlayerTurnQueue(),
					userName);
			otherPlayers.addFirst(userName);
			turnNode.setNextPlayer(otherPlayers);
			turnNode.requestOtherPlayerUseCard(match);
		}
		return true;
	}

}
