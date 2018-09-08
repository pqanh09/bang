package com.example.springboot.controller;

import org.springframework.stereotype.Controller;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {
	
//	@Autowired
//	UserService userService;
//	
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
//    
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor) {
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        if(!userService.getUserMap().containsKey(chatMessage.getSender())) {
//        	userService.getUserMap().put(chatMessage.getSender(), headerAccessor.getUser().getName());
//        }
//        return chatMessage;
//    }

}
