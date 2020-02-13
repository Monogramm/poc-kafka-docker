package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Chatroom;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Chatroom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

}
