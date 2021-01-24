package Tests;

import org.junit.Test;
import org.junit.jupiter.api.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAPI extends Base {

    String emptyJobBodyString = "<project><builders/><publishers/><buildWrappers/></project>";

    @Test
    @Order(1)
    public void getAllItem() {
        authorizedGiven()
                .when().get(basePath + "view/all/api/json")
                .then().log().body().statusCode(200);
    }

    @Test
    @Order(2)
    public void getAllItemWithoutAuthorization() {
        given()
                .when().get(basePath + "view/all")
                .then().log().body().statusCode(403);  //403 Forbidden
    }

    @Test
    @Order(3)
    public void createItem() {
        authorizedGiven()
                .body(emptyJobBodyString)
                .contentType("application/xml")
                .queryParam("name", "Test1")
                .when().post(basePath + "createItem/")
                .then().log().body().statusCode(200);
        authorizedGiven()
                .when().get(basePath + "view/all/api/json")
                .then().assertThat().body("jobs", hasItem(allOf(hasEntry("name", "Test1"))));
    }

    @Test
    @Order(4)
    public void createItemWithoutBody() {
        authorizedGiven()
                .contentType("application/xml")
                .queryParam("name", "Test3")
                .when().post(basePath + "createItem")
                .then().log().body().statusCode(500); //500 Server Error
    }

    @Test
    @Order(5)
    public void createItemWithoutName() {
        authorizedGiven()
                .body(emptyJobBodyString)
                .contentType("application/xml")
                .when().post(basePath + "createItem")
                .then().log().body().statusCode(400); //400 Bad Request
    }

    @Test
    @Order(6)
    public void disableItem() {
        authorizedGiven()
                .when().post(basePath + "view/all/job/Test1/disable")
                .then().log().body().statusCode(200);
        authorizedGiven()
                .when().get(basePath + "view/all/job/Test1/api/json")
                .then().log().body().assertThat().body("disabled", equalTo(true));
    }

    @Test
    @Order(7)
    public void enableItem() {
        authorizedGiven()
                .when().post(basePath + "view/all/job/Test1/enable")
                .then().log().body().statusCode(200);
        authorizedGiven()
                .when().get(basePath + "view/all/job/Test1/api/json")
                .then().assertThat().body("disabled", equalTo(false));
    }

    @Test
    @Order(8)
    public void copyItem() {
        authorizedGiven()
                .queryParam("name", "Test2")
                .queryParam("mode", "copy")
                .queryParam("from", "Test1")
                .contentType("application/xml")
                .when().post(basePath + "createItem")
                .then().log().body().statusCode(200);
        authorizedGiven()
                .when().get(basePath + "view/all/api/json")
                .then().assertThat().body("jobs", hasItem(allOf(hasEntry("name", "Test2"))));
    }

    @Test
    @Order(9)
    public void copyItemWithoutParameters() {
        authorizedGiven()
                .queryParam("from", "Test1")
                .contentType("application/xml")
                .body(emptyJobBodyString)
                .when().post(basePath + "createItem")
                .then().log().body().statusCode(400); //400 Bad Request
    }

    @Test
    @Order(10)
    public void getNotExistedItem() {
        authorizedGiven()
                .when().get(basePath + "view/all/job/Test555/")
                .then().log().body().statusCode(404);   //404 Not Found
    }

    @Test
    @Order(11)
    public void deleteItem() {
        authorizedGiven()
                .when().delete(basePath + "view/all/job/Test2/")
                .then().log().body().statusCode(204);
        authorizedGiven()
                .when().get(basePath + "view/all/api/json")
                .then().assertThat().body("jobs", not(hasItem(allOf(hasEntry("name", "Test2")))));
    }

    @Test
    @Order(12)
    public void deleteNotExistedItem() {
        authorizedGiven()
                .when().delete(basePath + "view/all/job/Test555/")
                .then().log().body().statusCode(404);   //404 Not Found
    }

    @Test
    @Order(13)
    public void jsonSchemaValidation() {
        authorizedGiven()
                .when().get(basePath + "view/all/job/Test1/api/json")
                .then().assertThat().body(matchesJsonSchemaInClasspath("json-schema.json"));
    }
}

