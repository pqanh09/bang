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
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class VeraCuster extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(VeraCuster.class);

	@Override
	public void useSkill() {
		logger.info("using VeraCuster Herro's Skill");

	}

	public VeraCuster() {
		this.name = "VeraCuster";
		this.skillDescription = "Description " + name;
		this.id = "VeraCuster";
		this.lifePoint = 3;
		this.setImage("Hero-VeraCuster.jpg");
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
			character.attachHero(targetCharacter.getHero(), false);
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), targetPlayer, null));
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			match.getCurrentTurn().run(match);
		}
		return true;
	}

}
