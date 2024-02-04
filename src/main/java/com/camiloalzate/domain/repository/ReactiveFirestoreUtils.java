package com.camiloalzate.domain.repository;

import com.google.api.core.ApiFuture;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class ReactiveFirestoreUtils {

    public static <T> Mono<T> monoFromApiFuture(ApiFuture<T> apiFuture) {
        return Mono.create(sink -> {
            CompletableFuture<T> completableFuture = apiFutureToCompletableFuture(apiFuture);
            completableFuture.whenComplete((result, throwable) -> {
                if (throwable != null) {
                    sink.error(throwable);
                } else {
                    sink.success(result);
                }
            });
        });
    }

    private static <T> CompletableFuture<T> apiFutureToCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        apiFuture.addListener(() -> {
            try {
                completableFuture.complete(apiFuture.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run);
        return completableFuture;
    }
}
