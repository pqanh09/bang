package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.model.card.Card.Suit;
import com.example.springboot.response.BarrelCardResponse;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CommonService;

public class Jourdonnais extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(Jourdonnais.class);

	@Override
	public void useSkill() {

	}

	public Jourdonnais() {
		this.name = "Jourdonnais";
		this.skillDescription = "Description " + name;
		this.id = "Jourdonnais";
		this.lifePoint = 4;
		this.setImage("Hero-Jourdonnais.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
		Card card = commonService.getFromNewCardList(match, 1).get(0);
		
		commonService.addToOldCardList(card, match);
		
		TurnNode turnNode = match.getCurrentTurn();
		if(Suit.hearts.equals(card.getSuit())) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skillresult", new UserResponse(ResponseType.Skill, userName, "Success"));
			if(match.getCurrentTurn().getCharacter().getHero() instanceof SlabTheKiller) {
				match.getCurrentTurn().getPlayerUsedMissed().add(userName);
			} else {
				turnNode.getNextPlayer().poll();
			}
		} else {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skillresult", new UserResponse(ResponseType.Skill, userName, "Fail"));
		}
		return true;
	}


}
