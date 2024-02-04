package com.camiloalzate.domain.repository;
import com.camiloalzate.application.dto.DocumentId;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.UUID;

public abstract class AbstractFirestoreReactiveRepository<T> {
    private static final Logger log = LogManager.getLogger(AbstractFirestoreReactiveRepository.class);
    private final CollectionReference collectionReference;
    private final String collectionName;
    private final Class<T> parameterizedType;

    protected AbstractFirestoreReactiveRepository(Firestore firestore, String collection) {
        this.collectionReference = firestore.collection(collection);
        this.collectionName = collection;
        this.parameterizedType = getParameterizedType();
    }

    private Class<T> getParameterizedType(){
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>)type.getActualTypeArguments()[0];
    }

    protected String getDocumentId(T t) {
        Object key;
        Class clzz = t.getClass();
        do{
            key = getKeyFromFields(clzz, t);
            clzz = clzz.getSuperclass();
        } while(key == null && clzz != null);

        if(key==null){
            return UUID.randomUUID().toString();
        }
        return String.valueOf(key);
    }

    private Object getKeyFromFields(Class<?> clazz, Object t) {

        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DocumentId.class))
                .findFirst()
                .map(field -> getValue(t, field))
                .orElse(null);
    }

    @Nullable
    private Object getValue(Object t, java.lang.reflect.Field field) {
        field.setAccessible(true);
        try {
            return field.get(t);
        } catch (IllegalAccessException e) {
            log.error("Error in getting documentId key", e);
        }
        return null;
    }

    public Mono<Boolean> save(T model) {
        String documentId = getDocumentId(model);
        ApiFuture<WriteResult> resultApiFuture = collectionReference.document(documentId).set(model);
        return ReactiveFirestoreUtils.monoFromApiFuture(resultApiFuture)
                .doOnSuccess(writeResult -> log.info("{}-{} saved at {}", collectionName, documentId, writeResult.getUpdateTime()))
                .thenReturn(true)
                .doOnError(e -> log.error("Error saving {}={} {}", collectionName, documentId, e.getMessage()))
                .onErrorReturn(false);
    }


    public Mono<Void> delete(String documentId) {
        ApiFuture<WriteResult> resultApiFuture = collectionReference.document(documentId).delete();
        return ReactiveFirestoreUtils.monoFromApiFuture(resultApiFuture).then();
    }

    public Flux<T> retrieveAll() {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = collectionReference.get();
        return ReactiveFirestoreUtils.<QuerySnapshot>monoFromApiFuture(querySnapshotApiFuture)
                .flatMapMany(querySnapshot -> Flux.fromIterable(querySnapshot.getDocuments()))
                .map(documentSnapshot -> documentSnapshot.toObject(parameterizedType));
    }


    public Mono<T> get(String documentId) {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = collectionReference.document(documentId).get();
        return ReactiveFirestoreUtils.<DocumentSnapshot>monoFromApiFuture(documentSnapshotApiFuture)
                .filter(DocumentSnapshot::exists)
                .map(documentSnapshot -> documentSnapshot.toObject(parameterizedType))
                .switchIfEmpty(Mono.error(new Exception("Document not found")));
    }

}
