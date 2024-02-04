package com.camiloalzate.infraestructure.persistence.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.firestore.credentials.location}")
    private String credentialsLocation;

    @Bean
    public Firestore firestoreOptions() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(credentialsLocation);
        return FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();
    }

}