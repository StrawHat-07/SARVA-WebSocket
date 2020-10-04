package com.example.messagingstompwebsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Controller
public class GreetingController {

	private static int score = 0;
	private static List<String> instructions = new ArrayList<String>();
	private static int round = 0;
	private int timeout = 10; //timeout in seconds
	private static long timeVal;
	private final SendMessageController a;
	private long timeoutCount = 0;
	Timeout timer;

	GreetingController(SendMessageController smp) {
		this.a = smp;
		this.timer = new Timeout();
	}


	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ServerMessage greeting(ClientMessage message) throws Exception {
//		System.out.println(timer.hasStarted);
		timeoutCount=0;
		if (timer.hasStarted) timer.cancelTimer();
		if(message.getName().equals("END#123")){
			round=0;
			timeoutCount=0;
			timer.hasStarted=false;
			score=0;
			instructions.clear();
			System.out.println("Ending game Session");
			return new ServerMessage("Okay ending the Game Session! ");
		}
		if (message.getName().equals("Bingo#123")) {

			System.out.println("Enter first instruction: ");
			Scanner myObj = new Scanner(System.in);
			String generatedString = myObj.nextLine();
			instructions.add(generatedString);
			timer = new Timeout(timeout);
			int currentRound = round +1;
			return new ServerMessage("Round " + currentRound + ". Starting the game. Enter the same instruction in the text area above: \"" + generatedString + "\" Timeout: " + timeout + " seconds.");
		}

		try{
			String b = instructions.get(round);
		}
		catch (Exception ex){
			System.out.println(
					"Round_alpha : " + round
 			);
			a.sendMessageError();
//			timer = new Timeout();
		}
		if (message.getName().equals(instructions.get(round))) {
			score++;

			Thread.sleep(500); // simulated delay
			a.sendMessages(score, 1);

		} else {
			score--;
			Thread.sleep(500); // simulated delay
			a.sendMessages(score, 0);

		}

		if (score == 10) {
			//End Game
			a.sendWin();
			round=0;
			timeoutCount=0;
			timer.hasStarted=false;
			score=0;
			instructions.clear();
			return new ServerMessage("Game Over!");
		}
		if (score == -3) {
			//End Game with Loss and Disconnect
			a.sendLoss();
			round=0;
			timeoutCount=0;
			timer.hasStarted=false;
			score=0;
			instructions.clear();
			return new ServerMessage("Game Over!");

		}
//		if(timeoutCount == 3){
//			a.sendLossbyTimeout();
//		}

		round++;
		System.out.println("Current Score: " + score + ". Enter next Instruction: ");
		Scanner myObj = new Scanner(System.in);
		String generatedString = myObj.nextLine();
		try {
			instructions.add(generatedString);
		}
		catch(Exception ex){
			System.out.println(
					"Round : " + round
			);
			a.sendMessageError();

		}


		timeVal = System.currentTimeMillis();
		timer = new Timeout(timeout);
//		timer.schedule();
		int currentRound = round +1;
		return new ServerMessage("Round "+currentRound +".  Next Instruction: \"" + generatedString + "\" Timeout: " + timeout + " seconds.");

	}

	public class Timeout {
		Timer timer;
		public boolean hasStarted;
		public int seconds;


		public Timeout() {
			this.hasStarted = false;
		}

		public Timeout(int seconds) {
			timer = new Timer();
			this.seconds=seconds;
			this.hasStarted = true;
			timer.schedule(new RemindTask(), seconds * 1000);
		}

		public void updateTimer() {
			timer.cancel();
			timer = new Timer();
		}

		public void schedule(){
			timer.schedule(new RemindTask(),seconds * 1000);
		}

		public void cancelTimer() {
			this.hasStarted = false;
			timer.cancel();
		}

		class RemindTask extends TimerTask {

			public void run() {

				System.out.println("Didnt get a response from client in " + timeout + " sec!");
				timeoutCount++;
				a.sendTimeout(score,timeoutCount);
				round++;
//				timer.cancel(); //Terminate the timer thread
				if(timeoutCount >= 3){
					a.sendLossbyTimeout();
					timeoutCount=0;
					hasStarted =false;
					round=0;
					score=0;
					instructions.clear();
					return;
				}
				System.out.println("Current Score: " + score + ". Enter next Instruction: ");
				Scanner myObj = new Scanner(System.in);
				String generatedString = myObj.nextLine();
				instructions.add(generatedString);
				try {
					timer.schedule(new RemindTask(), seconds * 1000);
				}
				catch(Exception ex){
					System.out.println(timer);

				}
				a.sendInstruction(generatedString,timeout,round);
//				timer = new Timeout(10);

			}
		}
	}
}
