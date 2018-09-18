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

public class ClausTheSaint extends Hero {
	private static final Logger logger = LoggerFactory.getLogger(ClausTheSaint.class);

	@Override
	public void useSkill() {
		logger.info("using ClausTheSaint Herro's Skill");

	}

	public ClausTheSaint() {
		this.name = "ClausTheSaint";
		this.skillDescription = "Description " + name;
		this.id = "ClausTheSaint";
		this.lifePoint = 3;
		this.setImage("Hero-ClausTheSaint.jpg");
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
		if(step == 1) {
			commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/skill", new HeroSkillResponse(ResponseType.Skill, userName, character.getHero(), null, null));
			List<Card> cards = commonService.getFromNewCardList(match, match.getPlayerTurnQueue().size() + 2);
			character.getCardsInHand().addAll(cards);
			List<String> otherPlayers = new ArrayList<>(BangUtils.getOtherPlayer(match.getPlayerTurnQueue(), userName));
			commonService.getSimpMessageSendingOperations().convertAndSendToUser(sessionId, "/queue/"+match.getMatchId()+"/skill",
					new SkillResponse(true, 2, otherPlayers , cards, character.getHero()));
		} else {
			for (Entry<String, Object> entry : others.entrySet()) {
				Character playerCharacter = match.getCharacterMap().get(entry.getKey());
				String sessionIdPlayer =  match.getUserMap().get(entry.getKey());
				playerCharacter.getCardsInHand().add(commonService.getCardInHand(character, (String) entry.getValue()));
				playerCharacter.setNumCardsInHand(playerCharacter.getCardsInHand().size());
				BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), playerCharacter, sessionIdPlayer);
			}
			character.setNumCardsInHand(character.getCardsInHand().size());
			BangUtils.notifyCharacter(commonService.getSimpMessageSendingOperations(), match.getMatchId(), character, sessionId);
			match.getCurrentTurn().setAlreadyGetCard(true);
			match.getCurrentTurn().run(match);
		}
		return true;
	}

}
