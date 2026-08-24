package com.oji.mini_crm_server.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactRepositoryTest {

    @Mock
    private ContactRepository contactRepository;

    @Test
    void existsByEmailAndDeletedFalse_proxyCheck() {
        when(contactRepository.existsByEmailAndDeletedFalse("a@b.com")).thenReturn(true);
        assertThat(contactRepository.existsByEmailAndDeletedFalse("a@b.com")).isTrue();
    }
}


