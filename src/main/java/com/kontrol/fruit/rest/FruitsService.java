package com.kontrol.fruit.rest;

import com.kontrol.fruit.model.Fruit;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "fruits-api")
public interface FruitsService {

    @GET
    @Path("all")
    List<Fruit> getAll(); //use pagination when the api supports it
}
