package com.example.springboot.command;

import com.example.springboot.request.Request;

public interface ActionCmd {
	void execute(Request request);
}
