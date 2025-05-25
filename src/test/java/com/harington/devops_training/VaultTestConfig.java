package com.harington.devops_training;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultTemplate;

@Configuration
public class VaultTestConfig {
    @Bean
    public VaultTemplate vaultTemplate() {
        // Mock VaultTemplate pour éviter les appels réels
        return Mockito.mock(VaultTemplate.class);
    }
}