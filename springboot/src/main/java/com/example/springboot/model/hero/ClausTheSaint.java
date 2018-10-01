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
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;

public class ClausTheSaint extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(ClausTheSaint.class);

	@Override
	public void useSkill() {
		logger.info("using ClausTheSaint Herro's Skill");

	}

	public ClausTheSaint() {
		this.name = "ClausTheSaint";
		this.skillDescription = "Description " + name;
		this.id = "ClausTheSaint";
		this.lifePoint = 3;
		this.setImage("Hero-ClausTheSaint.jpg");
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
			List<Card> cards = commonService.getFromNewCardList(match, match.getPlayerTurnQueue().size() + 1);
			character.getCardsInHand().addAll(cards);
			List<String> players = new ArrayList<>(match.getPlayerTurnQueue());
			players.add(userName);
			//auto
			turnNode.getTemp().clear();
			turnNode.setTemp(players);
			turnNode.getCardTemp().clear();
			turnNode.getCardTemp().addAll(cards);
			//
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2 , players, cards, character.getHero(), null));
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownStart, userName, 30));
			
		} else {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/countdown", new HeroSkillResponse(ResponseType.CountDownEnd, userName, 20));
			if(others != null &&  others.size() != turnNode.getTemp().size()) {
				String player, cardId;
				for (Entry<String, Object> entry : others.entrySet()) {
					player = (String) entry.getValue();
					if(player.equals(userName)) {
						continue;
					}
					cardId = entry.getKey();
					Character playerCharacter = match.getCharacterMap().get(player);
					String sessionIdPlayer =  match.getUserMap().get(player);
					playerCharacter.getCardsInHand().add(commonService.getCardInHand(character, cardId));
					playerCharacter.setNumCardsInHand(playerCharacter.getCardsInHand().size());
					commonService.notifyCharacter(match.getMatchId(), playerCharacter, sessionIdPlayer);
				}
			} else {
				//auto
				auto(match, character, commonService, turnNode, userName);
			}
			String serverMessage = "- Using " + character.getHero().getName() + "'skill to give a new card for each player.";
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
					new HeroSkillResponse(userName, "", "", serverMessage, character.getHero()));
			
			auto(match, character, commonService, turnNode, userName);
			character.setNumCardsInHand(character.getCardsInHand().size());
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			match.getCurrentTurn().setAlreadyGetCard(true);
			match.getCurrentTurn().run(match);
		}
		return true;
	}
	private void auto(Match match, Character character, CommonService commonService, TurnNode turnNode, Object userName) {
		String player;
		Card card;
		for (int i = 0; i < turnNode.getTemp().size(); i++) {
			player =  turnNode.getTemp().get(i);
			if(player.equals(userName)) {
				continue;
			}
			card = turnNode.getCardTemp().get(i);
			Character playerCharacter = match.getCharacterMap().get(player);
			String sessionIdPlayer =  match.getUserMap().get(player);
			playerCharacter.getCardsInHand().add(commonService.getCardInHand(character, card.getId()));
			playerCharacter.setNumCardsInHand(playerCharacter.getCardsInHand().size());
			commonService.notifyCharacter(match.getMatchId(), playerCharacter, sessionIdPlayer);
		}
		
		turnNode.getCardTemp().clear();
		turnNode.getTemp().clear();
	}

}
