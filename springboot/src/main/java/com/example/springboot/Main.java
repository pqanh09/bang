package com.example.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.example.springboot.response.UseCardResponse;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Map<String, Object> map = new HashMap<>();
////		String s = (String) map.get("a");
//		System.out.println(StringUtils.isBlank(s));
////		System.out.println(map.get("a"));
//		List<String> list = new ArrayList<>();
//		list.add("aaa");
//		list.add("bbb");
//		int num = list.size();
//		list.remove(0);
//		System.out.println(num);
//		System.out.println(list.size());
		
		LinkedList<String> playerTurnQueue = new LinkedList<>();
		playerTurnQueue.add("1");
		playerTurnQueue.add("2");
		playerTurnQueue.add("3");
		playerTurnQueue.add("4");
		playerTurnQueue.add("5");
		for (String string : playerTurnQueue) {
			System.out.println(string);
		}
		for (String string : playerTurnQueue) {
			System.out.println(string);
		}
		for (String string : playerTurnQueue) {
			System.out.println(string);
		}
		
//		String a = new String("aaa");
//		
//		updateString(a);
//		System.out.println(a);
//		if(StringUtils.isNoneBlank(targetUser)) {
//			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCardInTurn", new UseCardInTurnResponse(character.getUserName(), result, targetUser));
//		} else {
//			simpMessageSendingOperations.convertAndSend("/topic/"+match.getMatchId()+"/usedCard",
//					new UseCardResponse(character.getUserName(), result, null));
//		}
	}

	private static void updateString(String a) {
		a = "bbb";
		
	}

}
