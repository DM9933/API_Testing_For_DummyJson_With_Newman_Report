package me.selenium.POM;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class DummyJsonApiTest {

    static {
        RestAssured.baseURI = "https://dummyjson.com";
    }

    @Test
    public void testGetAllUsers() {
        Response response = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response Body: " + response.asPrettyString());
        assertNotNull(response.jsonPath().getList("users"), "User list should not be null");
        assertTrue(response.jsonPath().getList("users").size() > 0, "User list should not be empty");
    }

    @Test
    public void testGetSingleUser() {
        Response response = given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response Body: " + response.asPrettyString());

        assertEquals(response.jsonPath().getInt("id"), 1, "User ID should be 1");
        assertEquals(response.jsonPath().getString("firstName"), "Emily", "First name should be Emily");
        assertEquals(response.jsonPath().getString("lastName"), "Johnson", "Last name should be Johnson");
    }

    @Test
    public void testCreateUser() {
        String requestBody = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 30 }";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users/add")
                .then()
                .statusCode(201)
                .extract().response();

        System.out.println("Response Body: " + response.asPrettyString());
        assertEquals(response.jsonPath().getString("firstName"), "John", "First name should be John");
        assertEquals(response.jsonPath().getString("lastName"), "Doe", "Last name should be Doe");
        assertEquals(response.jsonPath().getInt("age"), 30, "Age should be 30");
    }

    @Test
    public void testUpdateUser() {
        String requestBody = "{ \"firstName\": \"Jane\", \"age\": 28 }";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/users/1")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response Body: " + response.asPrettyString());

        assertEquals(response.jsonPath().getString("firstName"), "Jane", "First name should be updated to Jane");
        assertEquals(response.jsonPath().getInt("age"), 28, "Age should be updated to 28");
    }

    @Test
    public void testDeleteUser() {
        Response response = given()
                .when()
                .delete("/users/1")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Response Body: " + response.asPrettyString());

        assertTrue(response.jsonPath().getBoolean("isDeleted"), "User should be marked as deleted");
    }
}
