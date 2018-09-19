package com.example.springboot.model.hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.MustangCard;
import com.example.springboot.model.card.ScopeCard;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class PatBrennan extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(PatBrennan.class);

	@Override
	public void useSkill() {
		logger.info("using PatBrennan Herro's Skill");

	}

	public PatBrennan() {
		this.name = "PatBrennan";
		this.skillDescription = "Description " + name;
		this.id = "PatBrennan";
		this.lifePoint = 4;
		this.setImage("Hero-PatBrennan.jpg");
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
		if(!turnNode.isAlreadyCheckedDynamite() || !turnNode.isAlreadyCheckedJail() || turnNode.isAlreadyGetCard()) {
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, false));
			return false;
		}
		if(step == 1) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<String> temp = new ArrayList<>(BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName));
			List<String> otherPlayers = new ArrayList<>();
			for (String player : temp) {
				if(match.getCharacterMap().get(player).getCardsInFront().isEmpty()) {
					continue;
				}
				otherPlayers.add(player);
			}
			if(otherPlayers.isEmpty()) {
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
						new SkillResponse(userName, false));
				return false;
			}
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2 , otherPlayers, null, character.getHero(), null));
		} else if(step == 2) {
			String targetPlayer =  (String) others.get("targetUser");
			Character targetCharacter = match.getCharacterMap().get(targetPlayer);
			turnNode.getNextPlayer().clear();
			turnNode.getNextPlayer().add(targetPlayer);
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 3 , null, targetCharacter.getCardsInFront(), character.getHero(), targetPlayer));
		} else {
			String targetPlayer =  turnNode.getNextPlayer().poll();
			Character targetCharacter = match.getCharacterMap().get(targetPlayer);
			String sessionIdTarget =  match.getUserMap().get(targetPlayer);
			@SuppressWarnings("unchecked")
			List<String> cardIds =  (List<String>) others.get("cards");
			Card card = commonService.getCardInFront(targetCharacter, cardIds.get(0));
			
			if (CardType.gun.equals(card.getCardType()) || card instanceof BarrelCard || card instanceof MustangCard || card instanceof ScopeCard || card instanceof JailCard || card instanceof DynamiteCard) {
				card.remove(targetCharacter);
			} 
			
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), targetCharacter, sessionIdTarget);
			
			character.getCardsInHand().add(card);
			character.setNumCardsInHand(character.getCardsInHand().size());
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), character, sessionId);
			
			turnNode.setAlreadyGetCard(true);
			
			turnNode.run(match);
		}
		return true;
	}

}
