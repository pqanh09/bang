package com.example.springboot.model.hero;

import java.util.ArrayList;
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
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useSkill(Match match, String userName, Character character, CommonService commonService,
			int step, Map<String, Object> others) {
		String sessionId = match.getUserMap().get(userName);
		if(step == 1) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<String> otherPlayers = new ArrayList<>(BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName));
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(true, 2, otherPlayers , null, character.getHero()));
		} else {
			Entry<String, Object> entry =  others.entrySet().iterator().next();
			Character targetCharacter = match.getCharacterMap().get(entry.getKey());
			String sessionIdTarget =  match.getUserMap().get(entry.getKey());
			character.getCardsInHand().add(commonService.getCardInHand(targetCharacter, (String) entry.getValue()));
			
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), targetCharacter, sessionIdTarget);
			
			character.setNumCardsInHand(character.getCardsInHand().size());
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), character, sessionId);
			
			match.getCurrentTurn().setAlreadyGetCard(true);
			
			match.getCurrentTurn().run(match);
		}
		return true;
	}

}
