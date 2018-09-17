package com.example.springboot.model.hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.response.HeroSkillResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.SkillResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class JoseDelgado extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(JoseDelgado.class);

	@Override
	public void useSkill() {
		logger.info("using JoseDelgado Herro's Skill");

	}

	public JoseDelgado() {
		this.name = "JoseDelgado";
		this.skillDescription = "Description " + name;
		this.id = "JoseDelgado";
		this.lifePoint = 4;
		this.setImage("Hero-JoseDelgado.jpg");
		this.autoUseSkill = false;
	}

	@Override
	public boolean useSkill(Card card) {
		// TODO Auto-generated method stub
		return false;
	}

	private List<Card> getBlueCards(List<Card> cards) {
		List<Card>  result = new ArrayList<>();
		for (Card card : cards) {
			switch (card.getCardType()) {
			case barrel:
				result.add(card);
				break;
			case otherviews:
				result.add(card);
				break;
			case viewothers:
				result.add(card);
				break;
			case gun:
				result.add(card);
				break;
			case magic:
				if(card instanceof JailCard || card instanceof DynamiteCard) {
					result.add(card);
					break;
				}
				break;
			default:
				break;
			}
		}
		return result;
	}
	@Override
	public boolean useSkill(Match match, Character character, CommonService commonService, int step,
			Map<String, Object> others) {
		String userName = character.getUserName();
		String sessionId = match.getUserMap().get(userName);
		TurnNode turnNode = match.getCurrentTurn();
		if(step == 1) {
			List<Card> cards = getBlueCards(character.getCardsInHand());
			if (turnNode.getJoseDelgado() >= 2 || !turnNode.isAlreadyCheckedDynamite() || !turnNode.isAlreadyCheckedJail()
					|| !turnNode.isAlreadyGetCard() || cards.isEmpty()) {
				commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
						new SkillResponse(false));
				return false;
			}
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(true, 2, null , cards, character.getHero()));
		} else {
			turnNode.setJoseDelgado(turnNode.getJoseDelgado() + 1);
			Entry<String, Object> entry =  others.entrySet().iterator().next();
			Card card =  commonService.getCardInHand(character, (String) entry.getValue());
			if(card == null) {
				logger.error("Error when perform JoseDelgado's skill");
				return false;
			}
			commonService.addToOldCardList(card, match);
			
			character.getCardsInHand().addAll(commonService.getFromNewCardList(match, 2));
			character.setNumCardsInHand(character.getCardsInHand().size());
			
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), character, sessionId);
			
			match.getCurrentTurn().run(match);
		}
		return true;
	}

}
