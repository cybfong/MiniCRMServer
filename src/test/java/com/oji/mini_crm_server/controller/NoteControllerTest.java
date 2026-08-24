package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Note;
import com.oji.mini_crm_server.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteControllerTest {

    @org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
    public static class Unit {

        @org.mockito.Mock
        private NoteService noteService;

        @org.mockito.InjectMocks
        private NoteController controller;

        @org.junit.jupiter.api.Test
        void getNotes_shouldReturnList() {
            Note n = new Note();
            n.setId(1L);
            n.setNoteText("hello");
            n.setCreatedAt(LocalDateTime.now());
            n.setUpdatedAt(LocalDateTime.now());

            when(noteService.getAllNotes()).thenReturn(List.of(n));

            var res = controller.getNotes();
            org.assertj.core.api.Assertions.assertThat(res).hasSize(1);
            org.assertj.core.api.Assertions.assertThat(res.get(0).getNoteText()).isEqualTo("hello");
        }

        @org.junit.jupiter.api.Test
        void createNote_shouldReturnCreated() {
            Note req = new Note();
            req.setNoteText("n1");

            Note saved = new Note();
            saved.setId(2L);
            saved.setNoteText("n1");

            when(noteService.createNote(any(Note.class))).thenReturn(saved);

            Note out = controller.createNote(req);
            org.assertj.core.api.Assertions.assertThat(out.getId()).isEqualTo(2L);
        }

        @org.junit.jupiter.api.Test
        void deleteNote_returnsCorrectMessage() {
            when(noteService.deleteNote(5L)).thenReturn(true);
            when(noteService.deleteNote(6L)).thenReturn(false);

            String ok = controller.deleteNote(5L);
            String notFound = controller.deleteNote(6L);

            org.assertj.core.api.Assertions.assertThat(ok).isEqualTo("Note deleted successfully");
            org.assertj.core.api.Assertions.assertThat(notFound).isEqualTo("Note not found");
        }
    }
}

