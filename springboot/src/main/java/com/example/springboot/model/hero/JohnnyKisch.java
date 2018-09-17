package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class JohnnyKisch extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(JohnnyKisch.class);

	@Override
	public void useSkill() {
		logger.info("using JohnnyKisch Herro's Skill");

	}

	public JohnnyKisch() {
		this.name = "JohnnyKisch";
		this.skillDescription = "Description " + name;
		this.id = "JohnnyKisch";
		this.lifePoint = 4;
		this.setImage("Hero-JohnnyKisch.jpg");
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
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
		// get cards for character;
		Card newCard = (Card) others.get("card");
		for (Entry<String, Character> entry : match.getCharacterMap().entrySet()) {
			if(!userName.equals(entry.getValue().getUserName())) {
				Card card = getCard(entry.getValue(), newCard.getName());
				if(card != null) {
					BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), entry.getValue(), match.getUserMap().get(entry.getValue().getUserName()));
					commonService.addToOldCardList(card, match);
				}
			}
		}
		return true;
	}
	private Card getCard(Character character, String cardName) {
		Card result = null;
		for (Card card : character.getCardsInFront()) {
			if(cardName.equals(card.getName())) {
				result = card;
				break;
			}
		}
		if(result != null) {
			character.getCardsInFront().remove(result);
		}
		return result;
		
	}

}
