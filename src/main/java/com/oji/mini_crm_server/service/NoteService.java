package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.model.Note;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.repo.CustomerRepository;
import com.oji.mini_crm_server.repo.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final CustomerRepository customerRepository;
    private final CurrentUserService currentUserService;

    public NoteService(NoteRepository noteRepository, CustomerRepository customerRepository,
                       CurrentUserService currentUserService) {
        this.noteRepository = noteRepository;
        this.customerRepository = customerRepository;
        this.currentUserService = currentUserService;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findByDeletedFalse();
    }

    public Note getNote(Long noteId) {
        return noteRepository.findByDeletedFalseAndId(noteId).orElseThrow(() -> new RuntimeException("Note not " +
                "found:" + " " + noteId));
    }

    @Transactional
    public Note createNote(Note note) {
        if (note.getCustomer() == null || note.getCustomer().getId() == null) {
            throw new RuntimeException("Customer is required");
        }

        Long customerId = note.getCustomer().getId();

        Customer customer =
                customerRepository.findByDeletedFalseAndId(customerId).orElseThrow(() -> new RuntimeException(
                        "Customer not found: " + customerId));

        note.setCustomer(customer);

        User currentUser = currentUserService.getCurrentUser();
        note.setCreatedBy(currentUser);
        note.setUpdatedBy(currentUser);

        LocalDateTime now = LocalDateTime.now();
        note.setCreatedAt(now);
        note.setUpdatedAt(now);

        note.setDeleted(false);

        return noteRepository.save(note);
    }

    @Transactional
    public Note updateNote(Long noteId, Note note) {
        Note existingNote = noteRepository.findByDeletedFalseAndId(noteId).orElseThrow(() -> new RuntimeException(
                "Note not found: " + noteId));

        if (note.getCustomer() == null || note.getCustomer().getId() == null) {
            throw new RuntimeException("Customer is required");
        }

        Long customerId = note.getCustomer().getId();

        User currentUser = currentUserService.getCurrentUser();

        Customer customer =
                customerRepository.findByDeletedFalseAndId(customerId).orElseThrow(() -> new RuntimeException(
                        "Customer not found: " + customerId));

        existingNote.setNoteText(note.getNoteText());
        existingNote.setCustomer(customer);
        existingNote.setUpdatedAt(LocalDateTime.now());
        existingNote.setUpdatedBy(currentUser);

        return noteRepository.save(existingNote);
    }

    @Transactional
    public boolean deleteNote(Long noteId) {
        Optional<Note> noteOptional = noteRepository.findByDeletedFalseAndId(noteId);

        if (noteOptional.isEmpty()) {
            return false;
        }

        Note note = noteOptional.get();

        User currentUser = currentUserService.getCurrentUser();

        note.setDeleted(true);
        note.setUpdatedBy(currentUser);
        note.setUpdatedAt(LocalDateTime.now());

        noteRepository.save(note);

        return true;
    }
}