package com.example.springboot.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.springboot.model.card.BarrelCard;
import com.example.springboot.model.card.Card;
import com.example.springboot.response.BarrelCardResponse;
import com.example.springboot.response.CardResponse;
import com.example.springboot.response.GetCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.TableService;

public class TurnNode {
	private boolean alreadyCheckedJail = false;
	private boolean alreadyCheckedDynamite = false;
	private boolean alreadyGetCard = false;
	private boolean alreadyUseBangCard = false;
	private Character character;
	private ResponseType action;
	private LinkedList<String> nextPlayer = new LinkedList<>();
	private List<Card> cardTemp = new ArrayList<>();
	private TableService tableService;
	private List<String> playerUsedBarrel = new ArrayList<>();

	
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
		tableService.getMessagingTemplate().convertAndSend("/topic/turn", new TurnResponse(ResponseType.Turn, character.getUserName()));
		this.alreadyCheckedDynamite = false;
		this.alreadyCheckedJail = false;
		this.alreadyGetCard = false;
		this.alreadyUseBangCard = false;
		this.action = ResponseType.Unknown;
		this.nextPlayer.clear();
		this.cardTemp.clear();
	}

	public TurnNode(TableService tableService) {
		super();
		this.tableService = tableService;
	}
	public void requestPlayerUseCard() {
		cardTemp.clear();
		nextPlayer.clear();
		tableService.getMessagingTemplate().convertAndSend("/topic/cardaction", new UserResponse(ResponseType.UseCard, character.getUserName()));
	}
	public void requestOtherPlayerUseCard() {
		if(ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action) || ResponseType.Duello.equals(action) || ResponseType.Indians.equals(action)) {
			// request player use cards
			String targetUser = nextPlayer.peek();
			if(targetUser != null) {
				boolean hasBarrel = false;
				if((ResponseType.Bang.equals(action) || ResponseType.Gatling.equals(action)) && !playerUsedBarrel.contains(targetUser)) {
					Character targetCharater = tableService.getCharacterMap().get(targetUser);
					for (Card card : targetCharater.getCardsInFront()) {
						if(card instanceof BarrelCard) {
							hasBarrel = true;
							break;
						}
					}
				}
				// request player use cards
				if(hasBarrel) {
					tableService.getMessagingTemplate().convertAndSend("/topic/action", new BarrelCardResponse(action, targetUser, true));
				} else {
					tableService.getMessagingTemplate().convertAndSend("/topic/action", new CardResponse(action, targetUser));
				}
			} else {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 87");
			}	
		} else if(ResponseType.Panic.equals(action) || ResponseType.CatPalou.equals(action)) {
			String targetUser = nextPlayer.peek();
			Character targetCharacter = tableService.getCharacterMap().get(targetUser);
			List<Card> cards = targetCharacter.getCardsInFront();
			// request player use cards
			tableService.getMessagingTemplate().convertAndSend("/topic/action", new GetCardResponse(character.getUserName(), action, targetUser, cards));
		} else if(ResponseType.GeneralStore.equals(action)) {
			// request player use cards
			String targetUser = nextPlayer.peek();
			if(targetUser != null) {
				// request player use cards
				tableService.getMessagingTemplate().convertAndSend("/topic/action", new CardResponse(ResponseType.GeneralStore, targetUser, cardTemp));
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
			tableService.getMessagingTemplate().convertAndSend("/topic/cardaction", new UserResponse(ResponseType.DrawCardJail, character.getUserName()));
		}
		//Check jail
		if(character.isBeJailed()) {
			tableService.getMessagingTemplate().convertAndSend("/topic/cardaction", new UserResponse(ResponseType.DrawCardDynamite, character.getUserName()));
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
			tableService.getMessagingTemplate().convertAndSend("/topic/cardaction", new UserResponse(ResponseType.GetCard, character.getUserName()));
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
