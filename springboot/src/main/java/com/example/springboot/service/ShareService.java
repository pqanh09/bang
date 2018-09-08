package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service("shareService")
public class ShareService {
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	public SimpMessageSendingOperations getMessagingTemplate() {
		return messagingTemplate;
	}

	public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	
}
