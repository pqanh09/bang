package com.example.springboot.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Character;
import com.example.springboot.model.TurnNode;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.TurnResponse;
import com.example.springboot.utils.BangUtils;

@Service("turnService")
public class TurnService {
	@Autowired
	private TableService tableService;
	private TurnNode currentTurn;
	public TurnNode getCurrentTurn() {
		return currentTurn;
	}
	public void setCurrentTurn(TurnNode currentTurn) {
		this.currentTurn = currentTurn;
	}
	
	public void createTurnNode(Character character) {
		if(this.currentTurn == null) {
			this.currentTurn = new TurnNode(tableService);
		}
		this.currentTurn.resetTurnNode(character);
	}
	public void createPlayerTurn() {
		String currentPlayerTurn = tableService.getPlayerTurnQueue().peekFirst();
		createTurnNode(tableService.getCharacterMap().get(currentPlayerTurn));
	}
	public void endTurn(String userName, boolean dynamite) {
		if(tableService.getPlayerTurnQueue().peek().equals(userName)){
			tableService.getMessagingTemplate().convertAndSend("/topic/turn", new TurnResponse(ResponseType.EndTurn, userName));
			tableService.getPlayerTurnQueue().poll();
			String nextPlayer = tableService.getPlayerTurnQueue().peek();
			if(StringUtils.isNotBlank(nextPlayer)) {
				Character nextCcharacter = tableService.getCharacterMap().get(nextPlayer);
				if(dynamite) {
					nextCcharacter.setHasDynamite(true);
					String sessionId = tableService.getUserMap().get(userName);
					BangUtils.notifyCharacter(tableService.getMessagingTemplate(), nextCcharacter, sessionId);
				}
				currentTurn.resetTurnNode(nextCcharacter);
				currentTurn.run();
				
			} else {
				tableService.getMessagingTemplate().convertAndSend("/topic/turn", new TurnResponse(ResponseType.Winner, userName));
				return;
			}
			tableService.getPlayerTurnQueue().add(userName);
		} else {
			System.out.println("Turn service endTurn ERROR @!@@@@@!");
		}
	}
	
}
