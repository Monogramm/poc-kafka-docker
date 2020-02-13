package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Chatroom;
import com.mycompany.myapp.repository.ChatroomRepository;
import com.mycompany.myapp.repository.search.ChatroomSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Chatroom}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ChatroomResource {

    private final Logger log = LoggerFactory.getLogger(ChatroomResource.class);

    private static final String ENTITY_NAME = "chatroom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatroomRepository chatroomRepository;

    private final ChatroomSearchRepository chatroomSearchRepository;

    public ChatroomResource(ChatroomRepository chatroomRepository, ChatroomSearchRepository chatroomSearchRepository) {
        this.chatroomRepository = chatroomRepository;
        this.chatroomSearchRepository = chatroomSearchRepository;
    }

    /**
     * {@code POST  /chatrooms} : Create a new chatroom.
     *
     * @param chatroom the chatroom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatroom, or with status {@code 400 (Bad Request)} if the chatroom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chatrooms")
    public ResponseEntity<Chatroom> createChatroom(@RequestBody Chatroom chatroom) throws URISyntaxException {
        log.debug("REST request to save Chatroom : {}", chatroom);
        if (chatroom.getId() != null) {
            throw new BadRequestAlertException("A new chatroom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Chatroom result = chatroomRepository.save(chatroom);
        chatroomSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/chatrooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chatrooms} : Updates an existing chatroom.
     *
     * @param chatroom the chatroom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatroom,
     * or with status {@code 400 (Bad Request)} if the chatroom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatroom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chatrooms")
    public ResponseEntity<Chatroom> updateChatroom(@RequestBody Chatroom chatroom) throws URISyntaxException {
        log.debug("REST request to update Chatroom : {}", chatroom);
        if (chatroom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Chatroom result = chatroomRepository.save(chatroom);
        chatroomSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, chatroom.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chatrooms} : get all the chatrooms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatrooms in body.
     */
    @GetMapping("/chatrooms")
    public List<Chatroom> getAllChatrooms() {
        log.debug("REST request to get all Chatrooms");
        return chatroomRepository.findAll();
    }

    /**
     * {@code GET  /chatrooms/:id} : get the "id" chatroom.
     *
     * @param id the id of the chatroom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatroom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chatrooms/{id}")
    public ResponseEntity<Chatroom> getChatroom(@PathVariable Long id) {
        log.debug("REST request to get Chatroom : {}", id);
        Optional<Chatroom> chatroom = chatroomRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(chatroom);
    }

    /**
     * {@code DELETE  /chatrooms/:id} : delete the "id" chatroom.
     *
     * @param id the id of the chatroom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chatrooms/{id}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Long id) {
        log.debug("REST request to delete Chatroom : {}", id);
        chatroomRepository.deleteById(id);
        chatroomSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/chatrooms?query=:query} : search for the chatroom corresponding
     * to the query.
     *
     * @param query the query of the chatroom search.
     * @return the result of the search.
     */
    @GetMapping("/_search/chatrooms")
    public List<Chatroom> searchChatrooms(@RequestParam String query) {
        log.debug("REST request to search Chatrooms for query {}", query);
        return StreamSupport
            .stream(chatroomSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
