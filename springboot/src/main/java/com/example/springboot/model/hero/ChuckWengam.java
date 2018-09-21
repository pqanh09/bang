package com.example.springboot.model.hero;

import java.util.Map;

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

public class ChuckWengam extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(ChuckWengam.class);

	@Override
	public void useSkill() {
		logger.info("using ChuckWengam Herro's Skill");

	}

	public ChuckWengam() {
		this.name = "ChuckWengam";
		this.skillDescription = "Description " + name;
		this.id = "ChuckWengam";
		this.lifePoint = 4;
		this.setImage("Hero-ChuckWengam.jpg");
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
		if(character.getLifePoint() <= 1 || !turnNode.isAlreadyCheckedDynamite() || !turnNode.isAlreadyCheckedJail() || !turnNode.isAlreadyGetCard()) {
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, false));
			return false;
		}
		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
		
		character.setLifePoint(character.getLifePoint() -1);

		character.getCardsInHand().addAll(commonService.getFromNewCardList(match, 2));
		character.setNumCardsInHand(character.getCardsInHand().size());
		
		commonService.notifyCharacter(match.getMatchId(), character, sessionId);
		
		match.getCurrentTurn().run(match);
		return true;
	}

}
