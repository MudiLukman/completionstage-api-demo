package com.kontrol.fruit;

import com.kontrol.fruit.model.Fruit;
import com.kontrol.model.PageParam;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fruits")
@Tag(name = "Fruits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FruitResource {

    @Inject FruitService fruitService;

    @POST
    public Response create(@Valid Fruit fruit) {
        return Response.status(Response.Status.CREATED)
                .entity(fruitService.create(fruit))
                .build();
    }

    @GET
    @Path("{id}")
    public Response find(@PathParam("id") long id) {
        return Response.ok(fruitService.find(id)).build();
    }

    @GET
    public Response findAll(@BeanParam PageParam pageParam) {
        return Response.ok(fruitService.findAll(pageParam)).build();
    }

    @GET
    @Path("import")
    public Response importAll() {
        fruitService.getAll();
        return Response.accepted().build(); //this operation will be completed in the future so return 202
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, Fruit fruit) {
        return Response.ok(fruitService.update(id, fruit)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        fruitService.delete(id);
        return Response.noContent().build();
    }
}
