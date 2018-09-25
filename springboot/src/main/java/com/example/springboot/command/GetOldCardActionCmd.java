package com.example.springboot.command;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.example.springboot.model.Character;
import com.example.springboot.model.Constants;
import com.example.springboot.model.Match;
import com.example.springboot.model.card.Card;
import com.example.springboot.model.hero.BillNoface;
import com.example.springboot.model.hero.BlackJack;
import com.example.springboot.model.hero.ClausTheSaint;
import com.example.springboot.model.hero.KitCarlson;
import com.example.springboot.model.hero.PixiePete;
import com.example.springboot.request.Request;
import com.example.springboot.response.OldCardResponse;
import com.example.springboot.service.CommonService;
import com.example.springboot.utils.BangUtils;

public class GetOldCardActionCmd extends AbsActionCmd implements ActionCmd {
	private static final Logger logger = LoggerFactory.getLogger(GetOldCardActionCmd.class);
	

	public GetOldCardActionCmd(CommonService commonService, SimpMessageSendingOperations simpMessageSendingOperations) {
		super(commonService, simpMessageSendingOperations);
	}

	@Override
	public void execute(Request request, Match match) throws Exception {
		OldCardResponse oldCardResponse = new OldCardResponse();
		Card card = match.getOldCards().peekLast();
		if(card != null) {
			oldCardResponse.getCards().add(card);
		}
		simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/oldcard", oldCardResponse);
	}

}
