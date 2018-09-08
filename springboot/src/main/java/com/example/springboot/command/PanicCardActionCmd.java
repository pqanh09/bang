package com.example.springboot.command;

import static org.hamcrest.CoreMatchers.instanceOf;

import org.apache.commons.lang3.StringUtils;

import com.example.springboot.model.Character;
import com.example.springboot.model.TurnNode;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.JailCard;
import com.example.springboot.model.card.Card.CardType;
import com.example.springboot.model.card.DynamiteCard;
import com.example.springboot.request.Request;
import com.example.springboot.service.HeroService;
import com.example.springboot.service.ShareService;
import com.example.springboot.service.TableService;
import com.example.springboot.service.TurnService;
import com.example.springboot.utils.BangUtils;

public class PanicCardActionCmd extends AbsActionCmd implements ActionCmd {

	public PanicCardActionCmd(TableService tableService, HeroService heroService, ShareService shareService,
			TurnService turnService) {
		super(tableService, heroService, shareService, turnService);
	}

	@Override
	public void execute(Request request) {
		TurnNode turnNode = turnService.getCurrentTurn();
		// current player
		String userName = request.getUser();
		String sessionId = tableService.getUserMap().get(userName);
		Character character = turnNode.getCharacter();
		// target player
		String targetPlayer = turnNode.getNextPlayer().peek();
		Character targetCharacter = tableService.getCharacterMap().get(targetPlayer);
		String sessionTargetId = tableService.getUserMap().get(targetPlayer);
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
			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), targetCharacter, sessionTargetId);
			character.getCardsInHand().add(card);
			BangUtils.notifyCharacter(tableService.getMessagingTemplate(), character, sessionId);
			turnNode.getNextPlayer().poll();
			if (turnNode.getNextPlayer().peek() == null) {
				// request player in turn continue using card
				turnNode.requestPlayerUseCard();
			} else {
				System.out.println("PanicCardActionCmd Error!!!!!!!!!!!!!!!!!!!");
			}
		} else {
			System.out.println("PanicCardActionCmd card null!!!!!!!!!!!!!!!!!!!");
		}
		
	}

}
