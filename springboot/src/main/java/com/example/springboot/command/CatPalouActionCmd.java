package com.example.springboot.command;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.MustangCard;
import com.example.springboot.model.card.ScopeCard;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.request.Request;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.CommonService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class CatPalouActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(CatPalouActionCmd.class);
	public CatPalouActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		TurnNode turnNode = match.getCurrentTurn();
		// target player
		String targetPlayer = turnNode.getNextPlayer().peek();
		Character targetCharacter = match.getCharacterMap().get(targetPlayer);
		String sessionTargetId = match.getUserMap().get(targetPlayer);
		Card card = null;
		if (StringUtils.isNotBlank(request.getId())) {
			card = commonService.getCardInFront(targetCharacter, request.getId());
//			BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), targetCharacter, sessionTargetId);
		} else {
			if (!targetCharacter.getCardsInHand().isEmpty()) {
				int rdCardNumber = new Random().nextInt(targetCharacter.getCardsInHand().size());
				card = commonService.getCardInHand(match, targetCharacter, targetCharacter.getCardsInHand().get(rdCardNumber).getId(), null);
			} else {
				logger.error("CatPalou card error cmd");
			}
		}
		if(card != null) {
			if (CardType.gun.equals(card.getCardType()) || card instanceof BarrelCard || card instanceof MustangCard || card instanceof ScopeCard || card instanceof JailCard || card instanceof DynamiteCard) {
				card.remove(targetCharacter);
			} 
			BangUtils.notifyCharacter(simpMessageSendingOperations, match.getMatchId(), targetCharacter, sessionTargetId);
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
