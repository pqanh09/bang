package com.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
		//pick hero
		//{"actionType":"PickHero","id":"VultureSam1"}
		
		//get cards
		//{"actionType":"GetCard"}
		
		//check card
		//{"actionType":"CheckCard","id":"11"}
		
		//use card
		//{"actionType":"UseCard","id":"11","noneResponse":false,"targetUser":"aa"}
		//{"actionType":"UseCard","id":"61"}
		//{"actionType":"UseCard","id":"47"}
		
		
		//{"actionType":"EndTurn"}
	}
}
