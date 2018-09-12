package com.example.springboot.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.BarrelCardResponse;
import com.example.springboot.response.CardResponse;
import com.example.springboot.response.GetCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.response.UserResponse;

public class TurnNode {
	private boolean alreadyCheckedJail = false;
	private boolean alreadyCheckedDynamite = false;
	private boolean alreadyGetCard = false;
	private boolean alreadyUseBangCard = false;
	private Character character;
	private ResponseType action;
	private LinkedList<String> nextPlayer = new LinkedList<>();
	private List<Card> cardTemp = new ArrayList<>();
	private List<String> playerUsedBarrel = new ArrayList<>();
	private SimpMessageSendingOperations simpMessageSendingOperations;
	private String matchId;
	
	public List<String> getPlayerUsedBarrel() {
		return playerUsedBarrel;
	}

	public void setPlayerUsedBarrel(List<String> playerUsedBarrel) {
		this.playerUsedBarrel = playerUsedBarrel;
	}

	public List<Card> getCardTemp() {
		return cardTemp;
	}

	public void setCardTemp(List<Card> cardTemp) {
		this.cardTemp = cardTemp;
	}

	public ResponseType getAction() {
		return action;
	}

	public void setAction(ResponseType action) {
		this.action = action;
	}

	public LinkedList<String> getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(LinkedList<String> nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}
	public void resetTurnNode(Character character) {
		this.character = character;
		this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/turn", new TurnResponse(ResponseType.Turn, character.getUserName()));
		this.alreadyCheckedDynamite = false;
		this.alreadyCheckedJail = false;
		this.alreadyGetCard = false;
		this.alreadyUseBangCard = false;
		this.action = ResponseType.Unknown;
		this.nextPlayer.clear();
		this.cardTemp.clear();
	}

	public TurnNode(SimpMessageSendingOperations simpMessageSendingOperations, String matchId) {
		super();
		this.simpMessageSendingOperations = simpMessageSendingOperations;
		this.matchId = matchId;
	}
	public void requestPlayerUseCard() {
		cardTemp.clear();
		nextPlayer.clear();
		simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.UseCard, character.getUserName()));
	}
	
	public void requestOtherPlayerUseCard(Match match) {
		if(ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action) || ResponseType.Duello.equals(action) || ResponseType.Indians.equals(action)) {
			// request player use cards
			String targetUser = nextPlayer.peek();
			if(targetUser != null) {
				boolean hasBarrel = false;
				if((ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action)) && !playerUsedBarrel.contains(targetUser)) {
					Character targetCharater = match.getCharacterMap().get(targetUser);
					for (Card card : targetCharater.getCardsInFront()) {
						if(card instanceof BarrelCard) {
							hasBarrel = true;
							break;
						}
					}
				}
				// request player use cards
				if(hasBarrel) {
					this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new BarrelCardResponse(action, targetUser, true));
				} else {
					this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new CardResponse(action, targetUser));
				}
			} else {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 87");
			}	
		} else if(ResponseType.Panic.equals(action) || ResponseType.CatPalou.equals(action)) {
			String targetUser = nextPlayer.peek();
			Character targetCharacter = match.getCharacterMap().get(targetUser);
			List<Card> cards = targetCharacter.getCardsInFront();
			// request player use cards
			this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new GetCardResponse(character.getUserName(), action, targetUser, cards));
		} else if(ResponseType.GeneralStore.equals(action)) {
			// request player use cards
			String targetUser = nextPlayer.peek();
			if(targetUser != null) {
				// request player use cards
				this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/action", new CardResponse(ResponseType.GeneralStore, targetUser, cardTemp));
			} else {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Null nextplayer");
			}	
		} else {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Null nextplayer");
		}
	}

	public void run() {
		//Check Dynamite
		if(character.isHasDynamite()) {
			this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.DrawCardDynamite, character.getUserName()));
			return;
		}
		//Check jail
		if(character.isBeJailed()) {
			this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.DrawCardJail, character.getUserName()));
			return;
		}
		//get cards
		getCard();
	}
	private void getCard() {
		if(!alreadyGetCard) {
			// request player get card
			// request player use cards
			cardTemp.clear();
			nextPlayer.clear();
			this.simpMessageSendingOperations.convertAndSend("/topic/"+this.matchId+"/cardaction", new UserResponse(ResponseType.GetCard, character.getUserName()));
		}
	}
	
	
	
	
	private void checkJail() {
		if(!alreadyCheckedJail) {
			// notify begin turn
			if(character.isBeJailed()) {
				//TODO
			}
			alreadyCheckedJail = true;
		}
		
	}
	private void checkDynamite() {
		if(!alreadyCheckedDynamite) {
			if(character.isHasDynamite()) {
				//TODO
			}
			alreadyCheckedDynamite = true;
		}
		
	}


	public boolean isAlreadyCheckedJail() {
		return alreadyCheckedJail;
	}


	public void setAlreadyCheckedJail(boolean alreadyCheckedJail) {
		this.alreadyCheckedJail = alreadyCheckedJail;
	}


	public boolean isAlreadyCheckedDynamite() {
		return alreadyCheckedDynamite;
	}


	public void setAlreadyCheckedDynamite(boolean alreadyCheckedDynamite) {
		this.alreadyCheckedDynamite = alreadyCheckedDynamite;
	}


	public boolean isAlreadyGetCard() {
		return alreadyGetCard;
	}


	public void setAlreadyGetCard(boolean alreadyGetCard) {
		this.alreadyGetCard = alreadyGetCard;
	}


	public boolean isAlreadyUseBangCard() {
		return alreadyUseBangCard;
	}


	public void setAlreadyUseBangCard(boolean alreadyUseBangCard) {
		this.alreadyUseBangCard = alreadyUseBangCard;
	}
	
}
