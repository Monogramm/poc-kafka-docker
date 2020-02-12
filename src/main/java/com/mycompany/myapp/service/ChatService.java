package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ChatMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing chatroom and their messages.
 */
@Service
@Transactional
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);

    private static final String KAFKA_CHAT_TOPIC = "kafka-chat"

    public List<String> getChatrooms() {
        // TODO Get list of available chatrooms
        return Collections.emptyList();
    }

    public ChatMessage sendChatroomMessage(String chatroom, ChatMessage body) {
        // Set message time and room
        body.setTime(new Date());
        body.setRoom(chatroom);

        // TODO Publish message to Kafka

        return body;
    }

    public List<ChatMessage> getChatroomMessages(String chatroom) {
        // TODO Get list of messages for chatroom
        return Collections.emptyList();
    }

}
