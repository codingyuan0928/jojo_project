package com.group1.gosports_jojo.service;

public interface EmailService {
  void sendMail(String to, String subject, String messageText);
}
