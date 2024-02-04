package com.camiloalzate.domain.repository;
import com.camiloalzate.application.dto.User;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractFirestoreReactiveRepository<User> {
    @Autowired
    protected UserRepository(Firestore firestore) {
        super(firestore, "users");
    }
}