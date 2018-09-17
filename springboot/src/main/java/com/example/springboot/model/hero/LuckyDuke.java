package com.example.springboot.model.hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.command.DynamiteActionCmd;
import com.example.springboot.command.JailActionCmd;
import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BangCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.MissedCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

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
	public boolean useSkill(Match match, String userName, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String sessionId = match.getUserMap().get(userName);
		TurnNode turnNode = match.getCurrentTurn();
		
		if(step == 1) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<Card> cards = commonService.getFromNewCardList(match, 2);
			turnNode.setCardTemp(cards);
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(true, 2, null, cards, character.getHero()));
		} else {
			String cardId =  (String) others.get(userName);
			Card card = null;
			for (Card cd : turnNode.getCardTemp()) {
				if(cd.getId().equals(cardId)) {
					card = cd;
					break;
				} else {
					commonService.addToOldCardList(cd, match);
				}
			}
			if(card == null) {
				logger.error("ERROR in LuckyDuke");
				return false;
			}
			turnNode.getCardTemp().clear();
			if(turnNode.isAlreadyCheckedDynamite()) {
				DynamiteActionCmd.checkDynamite(match, card, commonService);
				return true;
			}
			if(turnNode.isAlreadyCheckedJail()) {
				JailActionCmd.checkJail(match, card, commonService);
				return true;
			}
		}
		return true;
	}


}
