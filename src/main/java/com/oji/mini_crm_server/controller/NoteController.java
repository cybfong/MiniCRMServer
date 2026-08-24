package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Note;
import com.oji.mini_crm_server.service.NoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/api/notes")
    public List<Note> getNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/api/notes/{noteId}")
    public Note getNoteById(@PathVariable("noteId") Long noteId) {
        return noteService.getNote(noteId);
    }

    @PostMapping("/api/notes")
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @PutMapping("/api/notes/{noteId}")
    public Note updateNote(@PathVariable("noteId") Long noteId, @RequestBody Note note) {
        return noteService.updateNote(noteId, note);
    }

    @DeleteMapping("/api/notes/{noteId}")
    public String deleteNote(@PathVariable("noteId") Long noteId) {
        return noteService.deleteNote(noteId) ? "Note deleted successfully" : "Note not found";
    }
}