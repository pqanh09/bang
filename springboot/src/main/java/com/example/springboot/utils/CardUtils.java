package com.example.springboot.utils;

import com.example.springboot.model.card.Card;
import com.example.springboot.model.card.NoCard;

public class CardUtils {
	public static final Card losePointCard = new NoCard("LosePoint.jpg");
	public static final Card lose3PointCard = new NoCard("Lose3Point.jpg");
	public static final Card loseTurn = new NoCard("LoseTurn.jpg");
	public static final Card backCard = new NoCard("Back.jpg");
}
