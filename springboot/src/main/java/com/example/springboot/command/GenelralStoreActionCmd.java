package com.example.springboot.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.request.Request;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class GenelralStoreActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(GenelralStoreActionCmd.class);
	public GenelralStoreActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}
	@Override
	public void execute(Request request, Match match) throws Exception {
		TurnNode turnNode = match.getCurrentTurn();
		// current player
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		Character character = match.getCharacterMap().get(userName);
		List<Card> cards = turnNode.getCardTemp();
		Card card = null;
		for (Card cd : cards) {
			if(cd.getId().equals(request.getId())) {
				card = cd;
				break;
			}
		}
		if(card == null) {
			logger.error("GenelralStoreActionCmd Error!!!!!!!!!!!!!!!!!!!");
		} else {
			turnNode.getCardTemp().remove(card);
			character.getCardsInHand().add(card);
			commonService.notifyCharacter(match.getMatchId(), character, sessionId);
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				turnNode.requestOtherPlayerUseCard(match);
			}
		}
		
	}

}
