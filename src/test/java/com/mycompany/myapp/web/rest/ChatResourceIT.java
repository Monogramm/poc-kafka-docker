package com.mycompany.myapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mycompany.myapp.ApPoCApp;
import com.mycompany.myapp.service.ChatService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the ChatResource REST controller.
 *
 * @see ChatResource
 */
@SpringBootTest(classes = ApPoCApp.class)
public class ChatResourceIT {

    private MockMvc restMockMvc;

    @Autowired
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ChatResource chatResource = new ChatResource(this.chatService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(chatResource)
            .build();
    }

    /**
     * Test sendMessage
     */
    @Test
    public void testSendMessage() throws Exception {
        restMockMvc.perform(post("/api/chat/{chatroom}/message"))
            .andExpect(status().isOk());
    }

    /**
     * Test getChatrooms
     */
    @Test
    public void testGetChatrooms() throws Exception {
        restMockMvc.perform(get("/api/chat/"))
            .andExpect(status().isOk());
    }

    /**
     * Test getChatroom
     */
    @Test
    public void testGetChatroom() throws Exception {
        restMockMvc.perform(get("/api/chat/{chatroom}"))
            .andExpect(status().isOk());
    }
}
