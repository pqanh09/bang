package com.example.springboot.model.hero;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class ElGringo extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(ElGringo.class);

	@Override
	public void useSkill() {
		logger.info("using ElGringo Herro's Skill");

	}

	public ElGringo() {
		this.name = "ElGringo";
		this.skillDescription = "Description " + name;
		this.id = "ElGringo";
		this.lifePoint = 3;
		this.setImage("Hero-ElGringo.jpg");
	}

	@Override
	public boolean useSkill(Card card) {
		return false;
	}

	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
		// get cards from target character;
		Character targetCharacter = match.getCurrentTurn().getCharacter();
		if(targetCharacter.getCardsInHand().isEmpty()) {
			return false;
		}
		String serverMessage = "- Using " + character.getHero().getName() + "'skill.";
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill",
				new HeroSkillResponse(character.getUserName(), "", "", serverMessage, character.getHero()));
		int rdCardNumber = new Random().nextInt(targetCharacter.getCardsInHand().size());
		Card card = commonService.getCardInHand(targetCharacter, targetCharacter.getCardsInHand().get(rdCardNumber).getId());
		commonService.notifyCharacter(match.getMatchId(), targetCharacter, match.getUserMap().get(targetCharacter.getUserName()));
		character.getCardsInHand().add(card);
		character.setNumCardsInHand(character.getCardsInHand().size());
		commonService.notifyCharacter(match.getMatchId(), character, match.getUserMap().get(userName));
		return true;
	}


}
