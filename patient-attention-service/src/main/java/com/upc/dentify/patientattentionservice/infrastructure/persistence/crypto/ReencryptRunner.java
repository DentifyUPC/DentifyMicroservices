package com.upc.dentify.patientattentionservice.infrastructure.persistence.crypto;

import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.AnamnesisRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ReencryptRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ReencryptRunner.class);
    private final AnamnesisRepository repo;


    public ReencryptRunner(AnamnesisRepository repo) { this.repo = repo; }


    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting anamnesis backfill re-encrypt job");
        repo.findAll().forEach(r -> {
            String text = r.getAnamnesisText();
            if (text != null && !looksBase64(text)) {
                log.info("Re-encrypting anamnesis id={}", r.getId());
                r.setAnamnesisText(text); // converter will encrypt
                repo.save(r);
            }
        });
        log.info("Backfill finished. Remove/Re-disable ReencryptRunner after migration.");
    }


    private boolean looksBase64(String s) {
        if (s == null) return false;
        return s.matches("^[A-Za-z0-9+/]+={0,2}$");
    }
}
