package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Chatroom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Chatroom} entity.
 */
public interface ChatroomSearchRepository extends ElasticsearchRepository<Chatroom, Long> {
}
