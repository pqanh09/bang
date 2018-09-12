package com.example.springboot.command;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.request.Request;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class PanicCardActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(PanicCardActionCmd.class);
	public PanicCardActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) {
		TurnNode turnNode = match.getCurrentTurn();
		// current player
		String userName = request.getUser();
		String sessionId = match.getUserMap().get(userName);
		Character character = turnNode.getCharacter();
		// target player
		String targetPlayer = turnNode.getNextPlayer().peek();
		Character targetCharacter = match.getCharacterMap().get(targetPlayer);
		String sessionTargetId = match.getUserMap().get(targetPlayer);
		Card card = null;
		if (StringUtils.isNotBlank(request.getId())) {
			card = BangUtils.getCardInFront(targetCharacter, request.getId());
		} else {
			if (!targetCharacter.getCardsInHand().isEmpty()) {
				card = BangUtils.getCardInHand(targetCharacter, targetCharacter.getCardsInHand().get(0).getId());
			}
			if (card == null) {
				card = BangUtils.getCardInFront(targetCharacter, targetCharacter.getCardsInFront().get(0).getId());
			}
		}
		if(card != null) {
			if (CardType.gun.equals(card.getCardType())) {
				targetCharacter.setGun(1);
			} else if (CardType.barrel.equals(card.getCardType())) {
				targetCharacter.setBarrel(false);
			}  else if (CardType.otherviews.equals(card.getCardType())) {
				targetCharacter.setOthersView(targetCharacter.getOthersView()-1);
			}  else if (CardType.viewothers.equals(card.getCardType())) {
				targetCharacter.setViewOthers(targetCharacter.getViewOthers()-1);
			} 
			if(card instanceof JailCard) {
				targetCharacter.setBeJailed(false);
			} else if(card instanceof DynamiteCard) {
				targetCharacter.setHasDynamite(false);
			}
			BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), targetCharacter, sessionTargetId);
			character.getCardsInHand().add(card);
			BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), character, sessionId);
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				logger.error("PanicCardActionCmd Error!!!!!!!!!!!!!!!!!!!");
			}
		} else {
			logger.error("PanicCardActionCmd card null!!!!!!!!!!!!!!!!!!!");
		}
		
	}

}
