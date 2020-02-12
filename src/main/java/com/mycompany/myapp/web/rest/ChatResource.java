package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ChatMessage;
import com.mycompany.myapp.service.ChatService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing chat messages.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatResource {

    private final Logger log = LoggerFactory.getLogger(ChatResource.class);

    private final ChatService chatService;

    public ChatResource(ChatService chatService) {

        this.chatService = chatService;
    }

    /**
    * GET get chatrooms
    */
    @GetMapping("/")
    public List<String> getChatrooms() {
        return this.chatService.getChatrooms();
    }

    /**
    * POST send message to a chatroom
    */
    @PostMapping("/{chatroom}/message")
    public ChatMessage sendMessage(@PathVariable String chatroom, @RequestBody ChatMessage body) {
        return this.chatService.sendChatroomMessage(chatroom, body);
    }

    /**
    * GET get chatroom messages
    */
    @GetMapping("/{chatroom}")
    public List<ChatMessage> getMessages(@PathVariable String chatroom) {
        return this.chatService.getChatroomMessages(chatroom);
    }

}
