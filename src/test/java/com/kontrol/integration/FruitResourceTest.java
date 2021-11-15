package com.kontrol.integration;

import com.kontrol.fruit.model.Fruit;
import com.kontrol.fruit.model.Nutrition;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class FruitResourceTest {

    private Fruit fruit;

    @BeforeEach
    @Transactional
    public void setup() {
        fruit = new Fruit();
        fruit.id = 1L;
        fruit.name = "Cashew";

        Nutrition nutrition = new Nutrition();
        nutrition.setSugar(10);
        nutrition.setFat(10);
        nutrition.setProtein(6);
        nutrition.setCarbohydrates(7);
        nutrition.setCalories(8);
        fruit.nutritions = nutrition;
        fruit.persist();
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        Fruit.deleteAll();
    }

    @Test
    public void testCreate() {
        Fruit fruit = new Fruit();
        fruit.id = 2L;
        fruit.name = "Mango";
        fruit.order = "Order";
        fruit.genus = "Genus";
        fruit.family = "Family";

        Nutrition nutrition = new Nutrition();
        nutrition.setCalories(10);
        nutrition.setCarbohydrates(10);
        nutrition.setFat(7);
        nutrition.setProtein(7);
        nutrition.setSugar(12);

        fruit.nutritions = nutrition;

        String name = given()
                .body(fruit)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/fruits")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().body().jsonPath().get("name");

        Assertions.assertEquals(fruit.name, name);
    }

    @Test
    public void testCreateWithoutNutrition() {
        Fruit fruit = new Fruit();
        fruit.id = 2L;
        fruit.name = "Mango";
        fruit.order = "Order";
        fruit.genus = "Genus";
        fruit.family = "Family";

        given().body(fruit)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("fruits")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testGet() {
        String name = given()
                .pathParam("id", fruit.id)
                .when()
                .get("fruits/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath().get("name");

        Assertions.assertEquals(fruit.name, name);
    }

    @Test
    public void testGetNotFound() {
        given().pathParam("id", 7_373L)
                .when()
                .get("fruits/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindAll() {
        given().when()
                .get("fruits")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(not(hasSize(0)));
    }

    @Test
    public void testUpdate() {
        Fruit request = new Fruit();
        request.name = "Alphonse Mango";

        request.nutritions = fruit.nutritions;

        String name = given().pathParam("id", fruit.id)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put("fruits/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().jsonPath().getString("name");

        Assertions.assertEquals(request.name, name);
    }

    @Test
    public void testDelete() {
        given().pathParam("id", fruit.id)
                .when()
                .delete("fruits/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testDeleteNotFound() {
        given().pathParam("id", 82_929_289L)
                .when()
                .delete("fruits/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
