package com.example.springboot.model.hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class PedroRamirez extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(PedroRamirez.class);

	@Override
	public void useSkill() {

	}

	public PedroRamirez() {
		this.name = "PedroRamirez";
		this.skillDescription = "Description " + name;
		this.id = "PedroRamirez";
		this.lifePoint = 4;
		this.setImage("Hero-PedroRamirez.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, String userName, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		if(step == 1) {
			String sessionId = match.getUserMap().get(userName);
			Card card = match.getOldCards().pollLast();
			if(card == null) {
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
						new SkillResponse(false));
				return false;
			}
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			
			character.getCardsInHand().add(card);
			character.getCardsInHand().addAll(commonService.getFromNewCardList(match, 1));
			character.setNumCardsInHand(character.getCardsInHand().size());
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), character, match.getUserMap().get(userName));
			
			match.getCurrentTurn().setAlreadyGetCard(true);
			
			match.getCurrentTurn().run(match);
		}
		return true;
	}


}
