package com.example.springboot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.role.RoleType;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.OldCardResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UserResponse;
import com.example.springboot.utils.BangUtils;

@Service("tableService")
public class TableService {
	private Map<String, Character> characterMap = new HashMap<>();
	private LinkedList<Card> newCards = new LinkedList<>();
	private LinkedList<Card> oldCards = new LinkedList<>();
	private boolean putInPool = false;
	private List<Card> cardPool = new ArrayList<>();
	private Map<String, String> userMap = new HashMap<>();
	private Map<String, SimpMessageSendingOperations> messagingTemplateMap = new HashMap<>();
	private Map<String, String> sessionIdMap = new HashMap<>();
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	private LinkedList<String> playerTurnQueue = new LinkedList<>();

	private Map<Pair<String, String>, Integer> rangeMap = new HashMap<>();
	private void notifyPrivateCharacter(String userName, Character character) {
		String sessionId = userMap.get(userName);
		messagingTemplateMap.get(userName).convertAndSendToUser(sessionId, "/queue/character", new CharacterResponse(ResponseType.Character, userName, character));
	}
	public void playerDead(String userName, boolean beKilled) {
		Character character = characterMap.get(userName);
		character.setRoleImage(character.getRole().getImage());
		messagingTemplate.convertAndSend("/topic/character", new CharacterResponse(ResponseType.Dead, userName, character.getVO()));
//		BangUtils.notifyCharacter(messagingTemplate, character,userMap.get(userName));
		playerTurnQueue.remove(userName);
		addToOldCardList(character.getCardsInFront());
		addToOldCardList(character.getCardsInHand());
		updateRangeMap();
		
		if(beKilled) {
			String killingPlayer = playerTurnQueue.peek();
			Character killerCharacter = characterMap.get(killingPlayer);
			RoleType roleTypeDeathPlayer = character.getRole().getRoleType();
			
			if(RoleType.FUORILEGGE.equals(roleTypeDeathPlayer)) {
				messagingTemplate.convertAndSend("/topic/server", new UserResponse(ResponseType.Gitf, killerCharacter.getUserName()));
				// get cards for character;
				List<Card> cards = getFromNewCardList(Constants.DEFAULT_CARD);
				killerCharacter.getCardsInHand().addAll(cards);
				killerCharacter.setNumCardsInHand(killerCharacter.getCardsInHand().size());
				//udpate character for user 
				notifyPrivateCharacter(killingPlayer, killerCharacter);
			} else if(RoleType.VICE.equals(roleTypeDeathPlayer) && RoleType.SCERIFFO.equals(killerCharacter.getRole().getRoleType())) {
				messagingTemplate.convertAndSend("/topic/server", new UserResponse(ResponseType.LoseCard, killerCharacter.getUserName()));
				addToOldCardList(killerCharacter.getCardsInFront());
				addToOldCardList(killerCharacter.getCardsInHand());
				//udpate character for user 
				notifyPrivateCharacter(killingPlayer, killerCharacter);
			} else if(RoleType.SCERIFFO.equals(roleTypeDeathPlayer)) {
				List<String> remainPlayers = new ArrayList<>(playerTurnQueue);
				boolean hasFuorilegge = false;
				for (String player : remainPlayers) {
					if(characterMap.get(player).getRole().getRoleType().equals(RoleType.FUORILEGGE)) {
						hasFuorilegge = true;
						break;
					}
				}
				if(hasFuorilegge) {
					messagingTemplate.convertAndSend("/topic/server", new UserResponse(ResponseType.Winner, RoleType.FUORILEGGE.toString()));
				} else {
					messagingTemplate.convertAndSend("/topic/server", new UserResponse(ResponseType.Winner, RoleType.RINNEGATO.toString()));
				}
				//TODO notify end
				playerTurnQueue.clear();
				return;
			}
			// check end game
			List<String> remainPlayers = new ArrayList<>(playerTurnQueue);
			boolean hasFuorilegge = false;
			boolean hasRinnegato = false;
			for (String player : remainPlayers) {
				if(characterMap.get(player).getRole().getRoleType().equals(RoleType.FUORILEGGE)) {
					hasFuorilegge = true;
					break;
				}
				if(characterMap.get(player).getRole().getRoleType().equals(RoleType.RINNEGATO)) {
					hasRinnegato = true;
					break;
				}
			}
			if(!hasFuorilegge && hasRinnegato) {
				//continue the match between SCERIFFO and RINNEGATO
			} else if (!hasFuorilegge && !hasRinnegato){
				messagingTemplate.convertAndSend("/topic/server", new UserResponse(ResponseType.Winner, RoleType.SCERIFFO.toString()));
				//TODO notify end
				playerTurnQueue.clear();
			}
		}
	}

	
	public Map<String, SimpMessageSendingOperations> getMessagingTemplateMap() {
		return messagingTemplateMap;
	}


	public void setMessagingTemplateMap(Map<String, SimpMessageSendingOperations> messagingTemplateMap) {
		this.messagingTemplateMap = messagingTemplateMap;
	}


	public Map<Pair<String, String>, Integer> getRangeMap() {
		return rangeMap;
	}

	public void setRangeMap(Map<Pair<String, String>, Integer> rangeMap) {
		this.rangeMap = rangeMap;
	}

	public void addToOldCardList(Card card) {
		oldCards.add(card);
		while (oldCards.size() > 2) {
			cardPool.add(oldCards.poll());
		}
		messagingTemplate.convertAndSend("/topic/oldcard", new OldCardResponse(Arrays.asList(card)));
	}

	public void addToOldCardList(List<Card> cards) {
		oldCards.addAll(cards);
		while (oldCards.size() > 2) {
			cardPool.add(oldCards.poll());
		}
		messagingTemplate.convertAndSend("/topic/oldcard", new OldCardResponse(cards));
	}
	public List<Card> getFromNewCardList(int n) {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (newCards.size() < 10) {
				Collections.shuffle(cardPool);
				newCards.addAll(cardPool);
				cardPool.clear();
			}
			cards.add(newCards.pollFirst());
		}
		return cards;
	}

	public void updateRangeMap() {
		rangeMap.clear();
		List<String> list = new ArrayList<>(playerTurnQueue);
		for (int i = 0; i < list.size(); i++) {
			String begin = list.get(i);
			LinkedList<String> tmp = new LinkedList<>(playerTurnQueue);
			while (!tmp.peek().equals(begin)) {
				tmp.add(tmp.poll());
			}
			int range = 1;
			tmp.poll();
			String end;
			while (!tmp.isEmpty()) {
				end = tmp.poll();
				if (!rangeMap.containsKey(Pair.of(begin, end)) && !rangeMap.containsKey(Pair.of(end, begin))) {
					rangeMap.put(Pair.of(begin, end), range);
				} else {
					Pair<String, String> pair = rangeMap.containsKey(Pair.of(begin, end)) ? Pair.of(begin, end)
							: Pair.of(end, begin);
					int oldRange = rangeMap.get(pair);
					if (range < oldRange) {
						rangeMap.put(pair, range);
					}
				}
				range++;
			}
		}
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	public Map<String, String> getSessionIdMap() {
		return sessionIdMap;
	}

	public void setSessionIdMap(Map<String, String> sessionIdMap) {
		this.sessionIdMap = sessionIdMap;
	}

	public LinkedList<String> getPlayerTurnQueue() {
		return playerTurnQueue;
	}

	public void setPlayerTurnQueue(LinkedList<String> playerTurnQueue) {
		this.playerTurnQueue = playerTurnQueue;
	}

	public Map<String, Character> getCharacterMap() {
		return characterMap;
	}

	public void setCharacterMap(Map<String, Character> characterMap) {
		this.characterMap = characterMap;
	}

	public LinkedList<Card> getNewCards() {
		return newCards;
	}

	public void setNewCards(LinkedList<Card> newCards) {
		this.newCards = newCards;
	}

	public LinkedList<Card> getOldCards() {
		return oldCards;
	}

	public void setOldCards(LinkedList<Card> oldCards) {
		this.oldCards = oldCards;
	}

	public boolean isPutInPool() {
		return putInPool;
	}

	public void setPutInPool(boolean putInPool) {
		this.putInPool = putInPool;
	}

	public List<Card> getCardPool() {
		return cardPool;
	}

	public void setCardPool(List<Card> cardPool) {
		this.cardPool = cardPool;
	}

	public SimpMessageSendingOperations getMessagingTemplate() {
		return messagingTemplate;
	}

	public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	
}
