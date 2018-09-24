package com.example.springboot.model.hero;

import java.util.ArrayList;
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

public class JesseJones extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(JesseJones.class);

	@Override
	public void useSkill() {

	}

	public JesseJones() {
		this.name = "JesseJones";
		this.skillDescription = "Description " + name;
		this.id = "JesseJones";
		this.lifePoint = 4;
		this.setImage("Hero-JesseJones.jpg");
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
		if(!turnNode.isAlreadyCheckedDynamite() || !turnNode.isAlreadyCheckedJail() || turnNode.isAlreadyGetCard()) {
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, false));
			return false;
		}
		if(step == 1) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<String> otherPlayers = new ArrayList<>(BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName));
			//auto
			turnNode.getTemp().clear();
			turnNode.setTemp(otherPlayers);
			//
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2 , otherPlayers, null, character.getHero(), null));
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownStart, userName, 10));
		} else {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownEnd, userName, 20));
			String targetPlayer =  (String) others.get("targetUser");
			// auto
			if(StringUtils.isBlank(targetPlayer)) {
				List<String> otherPlayers = turnNode.getTemp();
				targetPlayer = otherPlayers.get(new Random().nextInt(otherPlayers.size()));
			}
			turnNode.getTemp().clear();
			//
			Character targetCharacter = match.getCharacterMap().get(targetPlayer);
			int rdCardNumber = new Random().nextInt(targetCharacter.getCardsInHand().size());
			Card card = commonService.getCardInHand(targetCharacter, targetCharacter.getCardsInHand().get(rdCardNumber).getId());
			commonService.notifyCharacter(match.getMatchId(), targetCharacter, match.getUserMap().get(targetCharacter.getUserName()));
			character.getCardsInHand().add(card);
			character.getCardsInHand().addAll(commonService.getFromNewCardList(match, 1));
			character.setNumCardsInHand(character.getCardsInHand().size());
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			
			turnNode.setAlreadyGetCard(true);
			
			turnNode.run(match);
		}
		return true;
	}


}
