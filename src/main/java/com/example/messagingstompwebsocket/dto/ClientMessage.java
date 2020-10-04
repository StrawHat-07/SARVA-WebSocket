package com.example.messagingstompwebsocket.dto;

public class ClientMessage {

	private String name;
	private long gameVariable;

	public ClientMessage() {
	}

	public ClientMessage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getGameVariable() {
		return gameVariable;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGameVariable(long gameVariable) {
		this.gameVariable = gameVariable;
	}
}
