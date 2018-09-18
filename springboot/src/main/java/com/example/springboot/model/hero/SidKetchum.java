package com.example.springboot.model.hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.RemoveCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
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
		if(step == 1) {
			List<Card> cards = new ArrayList<>();
			cards.addAll(character.getCardsInFront());
			cards.addAll(character.getCardsInHand());
			if(cards.size() < 2 || character.getLifePoint() < character.getCapacityLPoint() || !turnNode.isAlreadyCheckedDynamite() 
					|| !turnNode.isAlreadyCheckedJail() 
					|| !turnNode.isAlreadyGetCard()) {
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
						new SkillResponse(userName, false));
				return false;
			}
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(true, 2, null , cards, character.getHero()));
		} else {
			for (String cardId : others.keySet()) {
				Card  card = BangUtils.findCardInFront(character, cardId);
				if(card == null)  {
					card = BangUtils.findCardInHand(character, cardId);
				}
				if(card == null) {
					logger.error("Error when perform SidKetchum's skill");
					return false;
				}
				List<Card> cards = new ArrayList<>();
				cards.add(card);
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/removecard", new RemoveCardResponse(userName, cards));
			}
			if(others.keySet().size() > 2) {
				character.setLifePoint(character.getLifePoint() + 1);
			} else {
				logger.error("Error when perform SidKetchum's skill: size  < 2");
				return false;
			}
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), character, sessionId);
		}
		return true;
	}


}
