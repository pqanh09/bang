package com.example.springboot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.card.Card;
import com.example.springboot.response.CharacterResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UserResponse;

public class Match {
	public enum MatchStatus {
		playing, waiting;
	}
	private String matchId;
	private MatchStatus status = MatchStatus.waiting;
	private Map<String, Character> characterMap = new HashMap<>();
	private LinkedList<Card> newCards = new LinkedList<>();
	private LinkedList<Card> oldCards = new LinkedList<>();
	private boolean putInPool = false;
	private List<Card> cardPool = new ArrayList<>();
	private Map<String, String> userMap = new HashMap<>();
	private LinkedList<String> playerTurnQueue = new LinkedList<>();
	private Map<String, SimpMessageSendingOperations> messagingTemplateMap = new HashMap<>();
	private Map<Pair<String, String>, Integer> rangeMap = new HashMap<>();
	
	public Match(String matchId, String userName, String sessionId, SimpMessageSendingOperations messagingTemplate) {
		this.userMap.put(userName, sessionId);
		this.playerTurnQueue.add(userName);
		this.messagingTemplateMap.put(userName, messagingTemplate);
		this.matchId = matchId;
		this.status = MatchStatus.waiting;
	}
	public boolean addPlayer(String userName, String sessionId, SimpMessageSendingOperations messagingTemplate) {
		if(userMap.containsKey(userName)) {
			return false;
		} else {
			this.userMap.put(userName, sessionId);
			this.playerTurnQueue.add(userName);
			this.messagingTemplateMap.put(userName, messagingTemplate);
			return true;
		}
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
	
	public void disconnecPlayer(SimpMessageSendingOperations messagingTemplate, String playerName) {
		playerTurnQueue.remove(playerName);
		messagingTemplate.convertAndSend("/topic/"+matchId+"/server", new UserResponse(ResponseType.Leave, playerName));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<Pair<String, String>, Integer> getRangeMap() {
		return rangeMap;
	}
	public void setRangeMap(Map<Pair<String, String>, Integer> rangeMap) {
		this.rangeMap = rangeMap;
	}
	public MatchStatus getStatus() {
		return status;
	}
	public void setStatus(MatchStatus status) {
		this.status = status;
	}
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
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
	public Map<String, String> getUserMap() {
		return userMap;
	}
	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}
	public LinkedList<String> getPlayerTurnQueue() {
		return playerTurnQueue;
	}
	public void setPlayerTurnQueue(LinkedList<String> playerTurnQueue) {
		this.playerTurnQueue = playerTurnQueue;
	}
	public Map<String, SimpMessageSendingOperations> getMessagingTemplateMap() {
		return messagingTemplateMap;
	}
	public void setMessagingTemplateMap(Map<String, SimpMessageSendingOperations> messagingTemplateMap) {
		this.messagingTemplateMap = messagingTemplateMap;
	}
	
}
