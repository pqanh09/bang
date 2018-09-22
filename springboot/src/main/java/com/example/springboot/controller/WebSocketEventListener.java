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

        String username = (String) stompHeaderAccessor.getSessionAttributes().get(Constants.HEADER_ACCESSOR_USER);
        if(username != null) {
            logger.info("User Disconnected : " + username);
            String matchId = matchService.getUserMap().get(username);
            if(StringUtils.isNotBlank(matchId)) {
            	Match match = matchService.getMatchMap().get(matchId);
        		commonService.disconnecPlayer(match, username);
        		if(match.getPlayerTurnQueue().isEmpty()) {
            		matchService.getMatchMap().remove(matchId);
            		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/game", new MatchResponse(ResponseType.Delete, matchId, false));
            	} else {
            		commonService.getSimpMessageSendingOperations().convertAndSend("/topic/game", new MatchResponse(ResponseType.Update, matchId, false));
            	}
            	matchService.getUserMap().remove(username);
            }
            userService.getSessionIdMap().remove(userService.getUserMap().get(username));
            userService.getUserMap().remove(username);
        }
    }
}
