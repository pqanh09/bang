package com.example.springboot.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.springboot.model.Constants;
import com.example.springboot.model.Match;
import com.example.springboot.model.Match.MatchStatus;
import com.example.springboot.response.PlayerResponse;
import com.example.springboot.response.MatchResponse;
import com.example.springboot.response.ResponseType;
import com.example.springboot.response.UserResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.service.MatchService;
import com.example.springboot.service.UserService;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);


	@Autowired
	UserService userService;
	
	@Autowired
	MatchService matchService;
	
	@Autowired
	CommonService commonService;
	
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }
    
    @EventListener
    void handleSessionConnectedEvent(SessionConnectedEvent event) {
        // Get Accessor
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userName = (String) stompHeaderAccessor.getSessionAttributes().get(Constants.HEADER_ACCESSOR_USER);
        if(userName != null) {
            logger.info("User Disconnected : " + userName);
            String matchId = matchService.getUserMap().get(userName);
            if(StringUtils.isNotBlank(matchId)) {
            	Match match = matchService.getMatchMap().get(matchId);
            	if(match != null) {
            		commonService.disconnecPlayer(match, userName);
            		match.getUserMap().remove(userName);
            		match.getCharacterMap().remove(userName);
            		if(match.getPlayerTurnQueue().isEmpty()) {
            			match.getCharacterMap().clear();
            			match.getUserMap().clear();
            			match.setCurrentTurn(null);
                		matchService.getMatchMap().remove(matchId);
                		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/game", new MatchResponse(ResponseType.Update));
                	} else {
                		if(MatchStatus.waiting.equals(match.getStatus())) {
                			String host = match.getPlayerTurnQueue().peekFirst();
            				//update Host
            				commonService.getSimpMessageSendingOperations().convertAndSend("/topic/"+match.getMatchId()+"/server", new UserResponse(ResponseType.Update, host));
                			//update playerMap
                			commonService.sendCharacterMapForEachPlayer(match, null, userName, false);
                		}
                		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/game", new MatchResponse(ResponseType.Update));
                		commonService.callNextPlayerTurn(match, null);
                	}
            	}
            	matchService.getUserMap().remove(userName);
            }
            userService.getSessionIdMap().remove(userService.getUserMap().get(userName));
            userService.getUserMap().remove(userName);
        }
    }
}
