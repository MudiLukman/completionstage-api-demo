package com.kontrol.fruit;

import com.kontrol.fruit.model.Fruit;
import com.kontrol.fruit.rest.FruitsService;
import com.kontrol.model.PageParam;
import io.quarkus.panache.common.Sort;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
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
    public Fruit create(Fruit fruit) {
        fruit.persist();
        return fruit;
    }

    public void getAll() {
        threadContext.withContextCapture(CompletableFuture.supplyAsync(this::importFruits))
                .exceptionally(ex -> {
                    log.error("an error occurred: {}", ex.getCause().getMessage());
                    return Collections.emptyList();
                })
                .thenAccept(this::persist);
    }

    public Fruit findById(long id) {
        return (Fruit) Fruit.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    public Fruit find(long id) {
        return (Fruit) Fruit.find("id", id).firstResultOptional()
                .orElseThrow(NotFoundException::new);
    }

    public List<Fruit> findAll(PageParam pageParam) {
        return Fruit.findAll(Sort.ascending("id"))
                .page(pageParam.pageNumber, pageParam.pageSize)
                .list();
    }

    @Transactional
    public Fruit update(long id, Fruit request) {
        Fruit fruit = (Fruit) Fruit.findByIdOptional(id)
                .orElseThrow(NotFoundException::new);
        fruit.name = request.name;
        fruit.family = request.family;
        fruit.genus = request.genus;
        fruit.order = request.order;
        fruit.nutritions = request.nutritions;

        fruit.persist();
        return fruit;
    }

    @Transactional
    public void persist(List<Fruit> fruits) {
        Fruit.persist(fruits);
    }

    @Transactional
    public void delete(long id) {
        Fruit fruit = (Fruit) Fruit.findByIdOptional(id)
                .orElseThrow(NotFoundException::new);
        fruit.delete();
    }

    private List<Fruit> importFruits() {
        return fruitsService.getAll();
    }

}
