package com.example.springboot.command;

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.request.Request;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.CommonService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class CatPalouActionCmd extends AbsActionCmd implements ActionCmd {
	
	public CatPalouActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) {
		TurnNode turnNode = match.getCurrentTurn();
		// target player
		String targetPlayer = turnNode.getNextPlayer().peek();
		Character targetCharacter = match.getCharacterMap().get(targetPlayer);
		String sessionTargetId = match.getUserMap().get(targetPlayer);
		SimpMessageSendingOperations simpMessageSendingOperations = commonService.getSimpMessageSendingOperations();
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
			BangUtils.notifyCharacter(simpMessageSendingOperations, targetCharacter, sessionTargetId);
			commonService.addToOldCardList(card, match);
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				System.out.println("CatPalouActionCmd Error!!!!!!!!!!!!!!!!!!!");
			}
		} else {
			System.out.println("CatPalouActionCmd card null!!!!!!!!!!!!!!!!!!!");
		}
		
	}

}
