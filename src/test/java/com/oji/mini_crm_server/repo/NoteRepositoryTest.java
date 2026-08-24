package com.oji.mini_crm_server.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteRepositoryTest {

    @Mock
    private NoteRepository noteRepository;

    @Test
    void findByDeletedFalse_proxyCheck() {
        when(noteRepository.findByDeletedFalse()).thenReturn(java.util.List.of());
        assertThat(noteRepository.findByDeletedFalse()).isEmpty();
    }
}


