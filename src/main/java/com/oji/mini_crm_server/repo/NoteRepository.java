package com.oji.mini_crm_server.repo;

import com.oji.mini_crm_server.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByDeletedFalse();
    Optional<Note> findByDeletedFalseAndId(Long noteId);
}