package com.camiloalzate.application.dto;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collectionName = "users")
@Entity(name = "users")
public class User {

    @DocumentId
    private String id;
    private String email;
    private String name;

}
