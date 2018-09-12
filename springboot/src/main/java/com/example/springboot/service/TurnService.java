package com.example.springboot.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Character;
import com.example.springboot.model.Match;
import com.example.springboot.model.TurnNode;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.utils.BangUtils;

@Service("turnService")
public class TurnService {
	
//	public void createTurnNode(Match match, Character character) {
//		if(this.currentTurn == null) {
//			this.currentTurn = new TurnNode(tableService);
//		}
//		this.currentTurn.resetTurnNode(character);
//	}
//	public void createPlayerTurn() {
//		String currentPlayerTurn = tableService.getPlayerTurnQueue().peekFirst();
//		createTurnNode(tableService.getCharacterMap().get(currentPlayerTurn));
//	}
//	public void endTurn(String userName) {
//		if(tableService.getPlayerTurnQueue().peek().equals(userName)){
//			tableService.getMessagingTemplate().convertAndSend("/topic/turn", new TurnResponse(ResponseType.EndTurn, userName));
//			tableService.getPlayerTurnQueue().poll();
//			callNextPlayerTurn();
//			tableService.getPlayerTurnQueue().add(userName);
//		} else {
//			System.out.println("Turn service endTurn ERROR @!@@@@@!");
//		}
//	}
//	public void callNextPlayerTurn() {
//		String nextPlayer = tableService.getPlayerTurnQueue().peek();
//		if(StringUtils.isNotBlank(nextPlayer)) {
//			Character nextCharacter = tableService.getCharacterMap().get(nextPlayer);
//			currentTurn.resetTurnNode(nextCharacter);
//			currentTurn.run();
//		} else {
//			System.out.println("Turn service callNextPlayerTurn ERROR @!@@@@@!");
////			tableService.getMessagingTemplate().convertAndSend("/topic/turn", new TurnResponse(ResponseType.Winner, userName));
////			return;
//		}
//	}
//	public void callTurn() {
//		currentTurn.getNextPlayer().poll();
//		if (currentTurn.getNextPlayer().peek() == null) {
//			// request player in turn continue using card
//			currentTurn.requestPlayerUseCard();
//		} else {
//			currentTurn.requestOtherPlayerUseCard();
//		}
//	}
	
}
