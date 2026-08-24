package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.model.Note;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.repo.CustomerRepository;
import com.oji.mini_crm_server.repo.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private NoteService noteService;

    @Test
    void createNote_withoutCustomer_shouldThrow() {
        Note n = new Note();
        assertThrows(RuntimeException.class, () -> noteService.createNote(n));
    }

    @Test
    void createNote_success_shouldSave() {
        User u = new User();
        u.setId(1L);
        when(currentUserService.getCurrentUser()).thenReturn(u);

        Customer c = new Customer();
        c.setId(2L);
        when(customerRepository.findByDeletedFalseAndId(2L)).thenReturn(Optional.of(c));

        Note n = new Note();
        n.setCustomer(new Customer());
        n.getCustomer().setId(2L);
        n.setNoteText("t");

        when(noteRepository.save(any(Note.class))).thenAnswer(i -> {
            Note nn = i.getArgument(0);
            nn.setId(10L);
            return nn;
        });

        Note saved = noteService.createNote(n);
        assertThat(saved.getId()).isEqualTo(10L);
        assertThat(saved.getCreatedBy()).isEqualTo(u);
    }

    @Test
    void deleteNote_notFound_returnsFalse() {
        when(noteRepository.findByDeletedFalseAndId(5L)).thenReturn(Optional.empty());
        boolean res = noteService.deleteNote(5L);
        assertThat(res).isFalse();
    }

    @Test
    void deleteNote_found_marksDeleted() {
        User u = new User();
        u.setId(3L);
        when(currentUserService.getCurrentUser()).thenReturn(u);

        Note n = new Note();
        n.setId(5L);
        n.setDeleted(false);
        when(noteRepository.findByDeletedFalseAndId(5L)).thenReturn(Optional.of(n));

        boolean res = noteService.deleteNote(5L);
        assertThat(res).isTrue();
        assertThat(n.isDeleted()).isTrue();
        assertThat(n.getUpdatedBy()).isEqualTo(u);
        verify(noteRepository, times(1)).save(n);
    }
}

