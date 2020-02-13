package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ApPoCApp;
import com.mycompany.myapp.domain.Chatroom;
import com.mycompany.myapp.repository.ChatroomRepository;
import com.mycompany.myapp.repository.search.ChatroomSearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChatroomResource} REST controller.
 */
@SpringBootTest(classes = ApPoCApp.class)
public class ChatroomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ChatroomRepository chatroomRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ChatroomSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChatroomSearchRepository mockChatroomSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restChatroomMockMvc;

    private Chatroom chatroom;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatroomResource chatroomResource = new ChatroomResource(chatroomRepository, mockChatroomSearchRepository);
        this.restChatroomMockMvc = MockMvcBuilders.standaloneSetup(chatroomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chatroom createEntity(EntityManager em) {
        Chatroom chatroom = new Chatroom()
            .name(DEFAULT_NAME);
        return chatroom;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chatroom createUpdatedEntity(EntityManager em) {
        Chatroom chatroom = new Chatroom()
            .name(UPDATED_NAME);
        return chatroom;
    }

    @BeforeEach
    public void initTest() {
        chatroom = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatroom() throws Exception {
        int databaseSizeBeforeCreate = chatroomRepository.findAll().size();

        // Create the Chatroom
        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isCreated());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeCreate + 1);
        Chatroom testChatroom = chatroomList.get(chatroomList.size() - 1);
        assertThat(testChatroom.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Chatroom in Elasticsearch
        verify(mockChatroomSearchRepository, times(1)).save(testChatroom);
    }

    @Test
    @Transactional
    public void createChatroomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatroomRepository.findAll().size();

        // Create the Chatroom with an existing ID
        chatroom.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeCreate);

        // Validate the Chatroom in Elasticsearch
        verify(mockChatroomSearchRepository, times(0)).save(chatroom);
    }


    @Test
    @Transactional
    public void getAllChatrooms() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList
        restChatroomMockMvc.perform(get("/api/chatrooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get the chatroom
        restChatroomMockMvc.perform(get("/api/chatrooms/{id}", chatroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatroom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingChatroom() throws Exception {
        // Get the chatroom
        restChatroomMockMvc.perform(get("/api/chatrooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        int databaseSizeBeforeUpdate = chatroomRepository.findAll().size();

        // Update the chatroom
        Chatroom updatedChatroom = chatroomRepository.findById(chatroom.getId()).get();
        // Disconnect from session so that the updates on updatedChatroom are not directly saved in db
        em.detach(updatedChatroom);
        updatedChatroom
            .name(UPDATED_NAME);

        restChatroomMockMvc.perform(put("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedChatroom)))
            .andExpect(status().isOk());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeUpdate);
        Chatroom testChatroom = chatroomList.get(chatroomList.size() - 1);
        assertThat(testChatroom.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Chatroom in Elasticsearch
        verify(mockChatroomSearchRepository, times(1)).save(testChatroom);
    }

    @Test
    @Transactional
    public void updateNonExistingChatroom() throws Exception {
        int databaseSizeBeforeUpdate = chatroomRepository.findAll().size();

        // Create the Chatroom

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatroomMockMvc.perform(put("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Chatroom in Elasticsearch
        verify(mockChatroomSearchRepository, times(0)).save(chatroom);
    }

    @Test
    @Transactional
    public void deleteChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        int databaseSizeBeforeDelete = chatroomRepository.findAll().size();

        // Delete the chatroom
        restChatroomMockMvc.perform(delete("/api/chatrooms/{id}", chatroom.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Chatroom in Elasticsearch
        verify(mockChatroomSearchRepository, times(1)).deleteById(chatroom.getId());
    }

    @Test
    @Transactional
    public void searchChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);
        when(mockChatroomSearchRepository.search(queryStringQuery("id:" + chatroom.getId())))
            .thenReturn(Collections.singletonList(chatroom));
        // Search the chatroom
        restChatroomMockMvc.perform(get("/api/_search/chatrooms?query=id:" + chatroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
