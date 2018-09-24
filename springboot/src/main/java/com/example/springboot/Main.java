package com.example.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		String s = (String) map.get("a");
		System.out.println(StringUtils.isBlank(s));
		System.out.println(map.get("a"));
		List<String> list = new ArrayList<>();
		list.add("aaa");
		String s1 = list.get(0);
		list.remove(s1);
		System.out.println(s1);
	}

}
