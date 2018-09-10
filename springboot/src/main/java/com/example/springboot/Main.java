package com.example.springboot;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public static void main(String[] args) {
		
//		Runnable runnable = new Runnable() {
//		      public void run() {
//		        // task to run goes here
//		        System.out.println("Hello !!");
//		      }
//		    };
		    ScheduledExecutorService service = Executors
		                    .newSingleThreadScheduledExecutor();
		    service.scheduleAtFixedRate(new WorkerThread(10, service), 10, 1, TimeUnit.SECONDS);
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
//			//System User
//			File systemUserFile = ResourceUtils.getFile("classpath:cards.properties");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// TODO Auto-generated method stub
//		List<String> l = new ArrayList<>();
//		l.add("1");
//		l.add("2");
//		l.add("1");
//		l.add("3");
//		l.add("4");
//		l.add("15");
//		l.add("1");
//		System.out.println(l);
//		l.remove(1);
//		System.out.println(l);
//		System.out.println(l.subList(0, 1));
//		System.out.println(l);
//		Collections.shuffle(l);
//		System.out.println(l);

//		Queue myQueue = new LinkedList(); 
//
//		// add elements in the queue using offer() - return true/false
//		myQueue.offer("Monday");
//		myQueue.offer("Thusday");
//		boolean flag = myQueue.offer("Wednesday");
//		
//		System.out.println("Wednesday inserted successfully? "+flag);
//		
//		// add more elements using add() - throws IllegalStateException
//		try {
//			myQueue.add("Thursday");
//			myQueue.add("Friday");
//			myQueue.add("Weekend");
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("Pick the head of the queue: " + myQueue.peek());
//		
//		String head = null;
//		try {
//			// remove head - remove()
//			head = (String) myQueue.remove();
//			System.out.print("1) Push out " + head + " from the queue "); 
//			System.out.println("and the new head is now: "+myQueue.element());
//		} catch (NoSuchElementException e) {
//			e.printStackTrace();
//		}
//		
//		// remove the head - poll()
//		head = (String) myQueue.poll();
//		System.out.print("2) Push out " + head + " from the queue");
//		System.out.println("and the new head is now: "+myQueue.peek());
//		
//		// find out if the queue contains an object
//		System.out.println("Does the queue contain 'Weekend'? " + myQueue.contains("Weekend"));
//		System.out.println("Does the queue contain 'Monday'? " + myQueue.contains("Monday"));

//		LinkedList<String> myQueue = new LinkedList<>(); 
//		myQueue.add("0");
//		myQueue.add("1");
//		myQueue.add("2");
//		myQueue.add("3");
//		myQueue.add("4");
//		myQueue.add("5");
//		
//		myQueue.add("6");
//		myQueue.add("7");
//		System.out.println(myQueue);
//		List<String> aaa = new ArrayList<>();
//		aaa.add("9");
//		aaa.add("8");
//		List<String> bbb = myQueue.subList(0, 2);
//		System.out.println(bbb);
//		System.out.println(myQueue);
//		myQueue.addAll(aaa);
//		System.out.println(myQueue);

//		Collections.shuffle(myQueue);
//		myQueue.
		
//		LinkedList<String> playerTurn = new LinkedList<>();
//		boolean foundSceriffo = false;
//		List<String> players = new ArrayList<>();
//		players.add("1");
//		players.add("2");
//		players.add("3");
//		players.add("4");
//		players.add("5");
//		players.add("6");
//		players.add("7");
//		players.add("8");
//		List<String> playerNotYetInTurn = new ArrayList<>();
//		for (String pl : players) {
//			if (foundSceriffo) {
//				playerTurn.add(pl);
//			} else {
//				if (pl == "5") {
//					foundSceriffo = true;
//					playerTurn.add(pl);
//				} else {
//					playerNotYetInTurn.add(pl);
//				}
//			}
//		}
//		System.out.println(playerTurn);
//		System.out.println(playerNotYetInTurn);
//		playerTurn.addAll(playerNotYetInTurn);
//		System.out.println(playerTurn);
//		String player = playerTurn.pollFirst();
//		System.out.println(player);
//		System.out.println(playerTurn);
//		playerTurn.add(player);
//		System.out.println(playerTurn);
		
//		Card card = new BarrelCard(Suit.hearts, "aaa");
//		System.out.println(card);
//		Map<String, Card> map = new HashMap<>();
//		map.put("1", card);
//		
//		Card card1 = map.get("1");
//		card1.setName("new");
//		System.out.println(card1);
//		System.out.println(map);
//		System.out.println(map.get("1"));
//		System.out.println(card);
//		if(card.getClass().equals(BarrelCard.class)) {
//			System.out.println("#########");
//		}
		
//		LinkedList<String> myQueue = new LinkedList<>(); 
//		myQueue.add("aa");
//		myQueue.add("ss");
//		myQueue.add("dd");
//		myQueue.add("ff");
////		myQueue.add("E");
////		myQueue.add("F");
////		myQueue.add("G");
//		
//		Map<Pair<String, String>, Integer> rangeMap = new HashMap<>();
//		
//		List<String> list =  new ArrayList<>(myQueue);
//		for (int i = 0; i < list.size(); i++) {
//			String begin = list.get(i);
//			LinkedList<String> tmp = new LinkedList<>(myQueue); 
//			while(!tmp.peek().equals(begin)) {
//				tmp.add(tmp.poll());
//			}
//			int range = 1;
//			tmp.poll();
//			String end;
//			while(!tmp.isEmpty()) {
//				end = tmp.poll();
//				if(!rangeMap.containsKey(Pair.of(begin, end)) && !rangeMap.containsKey(Pair.of(end, begin))) {
//					rangeMap.put(Pair.of(begin, end), range);
//				} else {
//					Pair<String, String> pair = rangeMap.containsKey(Pair.of(begin, end)) ? Pair.of(begin, end) : Pair.of(end, begin);
//					int oldRange = rangeMap.get(pair);
//					if(range < oldRange) {
//						rangeMap.put(pair, range);
//					}
//				}
//				range ++;
//			}
//		}
//		System.out.println(rangeMap);
		
		
		
		
		
		
		
	
//		int n = myQueue.size()*2;
//		
//		
//		String before = null;
//		for (int i = 0; i < n; i++) {
//			String current = myQueue.pollFirst();
//			if(StringUtils.isNotBlank(before)) {
//				if(!rangeMap.containsKey(Pair.of(current, before)) && !rangeMap.containsKey(Pair.of(before, current))) {
//					rangeMap.put(Pair.of(current, before), 1);
//				}
//				
//			}
//			before = current;
//			myQueue.add(current);
//		}
//		
//		System.out.println(rangeMap);
//		
//		String begin = "5";
//		
//		LinkedList<String> tmp = new LinkedList<>(myQueue); 
//		System.out.println(tmp);
		
		
//		Pair<String, String> pair = Pair.of("0", "1") ;
//		Map<Pair<String, String>, Integer> rangeMap = new HashMap<>();
//		rangeMap.put(Pair.of("0", "1"), 1);
//		System.out.println(rangeMap.get(Pair.of("0", "1")));
//		System.out.println(rangeMap.get(Pair.of("1", "0")));
	}

}
