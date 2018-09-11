package com.example.springboot;

import java.util.concurrent.ScheduledExecutorService;

public class WorkerThread implements Runnable{
	private int maxCount;
	private int count = 0;
	private ScheduledExecutorService service;
	public WorkerThread(int maxCount, ScheduledExecutorService service) {
		super();
		this.maxCount = maxCount;
		this.service = service;
	}
	@Override
	public void run() {
		System.out.println("Hello !!");
		this.count ++;
		if(this.count >= this.maxCount) {
			service.shutdown();
		}
	}
	
	
}
