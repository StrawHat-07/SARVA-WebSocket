package com.example.messagingstompwebsocket.controller;


import com.example.messagingstompwebsocket.dto.ServerMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class SendMessageController{

    private final SimpMessagingTemplate simpMessagingTemplate;
    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/greetings";

    SendMessageController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
//"Hallo " + " at " + new Date().toString()
    public void sendMessages(long score, long flag) {
        ServerMessage mess;
//                = new ServerMessage("hello");
        if(flag==1)
           mess = new ServerMessage(  "Correct Ans! Current Score: " + score + "");
        else
          mess = new ServerMessage( "Wrong Ans! Current Score: " + score + "");
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,mess);
    }

    public void sendWin(){
        ServerMessage mess = new ServerMessage("Congratulations! You have won the game.");
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,mess);
    }

    public void sendLoss(){
        ServerMessage mess = new ServerMessage("Oops! You have lost the game. ");
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,mess);
    }

    public void sendLossbyTimeout(){
        ServerMessage mess = new ServerMessage("Oops! You have lost the game. Due to 3 continuous timeouts.");
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,mess);
    }

    public void sendInstruction(String generatedString, long timeout, long round){
        long currentRound = round +1;
        ServerMessage mess = new ServerMessage("Round "+currentRound +". Next Instruction: \"" + generatedString + "\" Timeout: "  + timeout + " seconds.");
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,mess);
    }

    public void sendMessageError(){
        ServerMessage mess = new ServerMessage("Pls wait for server to send instruction.");
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,mess);
    }

    public void sendTimeout(long score, long count){
        ServerMessage mess = new ServerMessage(("Timeout has occured! Current Score: "+ score + " Continuous Timeout count: " + count));
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,mess);
    }


}