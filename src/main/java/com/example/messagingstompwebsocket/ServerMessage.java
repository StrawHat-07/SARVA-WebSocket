package com.example.messagingstompwebsocket;

public class ServerMessage {

	private String content;

	private long gameVariable;

	public ServerMessage() {
	}

	public ServerMessage(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

}
