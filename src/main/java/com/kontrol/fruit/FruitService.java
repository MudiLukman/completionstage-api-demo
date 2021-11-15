package com.kontrol.fruit;

import com.kontrol.fruit.model.Fruit;
import com.kontrol.fruit.rest.FruitsService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@ApplicationScoped
public class FruitService {

    @Inject
    @RestClient
    FruitsService fruitsService;

    @Inject
    ThreadContext threadContext; //needed for shared security context, db transaction context e.t.c of the invoking thread

    @Transactional
    public void getAll() {
        threadContext.withContextCapture(CompletableFuture.supplyAsync(this::getFruits))
                .exceptionally(ex -> {
                    log.error("an error occurred: {}", ex.getCause().getMessage());
                    return Collections.emptyList();
                })
                .thenAccept(Fruit::persist);
    }

    private List<Fruit> getFruits() {
        return fruitsService.getAll();
    }

}
