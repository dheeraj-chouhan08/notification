package com.assignment.notification.models;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageSentResponse {
 private String request_id;
 private String comments;

 public MessageSentResponse(String request_id){
     this.comments = "Successfully Sent";
     this.request_id = request_id;
 }



}
