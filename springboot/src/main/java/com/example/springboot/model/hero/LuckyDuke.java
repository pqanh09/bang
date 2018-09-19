package com.example.springboot.model.hero;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.command.DynamiteActionCmd;
import com.example.springboot.command.JailActionCmd;
import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;

public class LuckyDuke extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(LuckyDuke.class);

	@Override
	public void useSkill() {

	}

	public LuckyDuke() {
		this.name = "LuckyDuke";
		this.skillDescription = "Description " + name;
		this.id = "LuckyDuke";
		this.lifePoint = 4;
		this.setImage("Hero-LuckyDuke.jpg");
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
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<Card> cards = commonService.getFromNewCardList(match, 2);
			turnNode.setCardTemp(cards);
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(userName, true, 2, null, cards, character.getHero(), null));
		} else {
			List<String> cardIds =  (List<String>) others.get("cards");
			Card card = null;
			for (Card cd : turnNode.getCardTemp()) {
				if(cd.getId().equals(cardIds.get(0))) {
					card = cd;
					break;
				} else {
					commonService.addToOldCardList(cd, match);
				}
			}
			if(card != null) {
				turnNode.getCardTemp().clear();
				if(character.isHasDynamite()) {
					DynamiteActionCmd.checkDynamite(match, card, commonService);
					return true;
				}
				if(character.isBeJailed()) {
					JailActionCmd.checkJail(match, card, commonService);
					return true;
				}
			} else {
				logger.error("ERROR in LuckyDuke");
				return false;
			}
			
		}
		return true;
	}


}
