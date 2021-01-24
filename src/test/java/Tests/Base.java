package Tests;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Base {

    String authorizationHeader = "Basic YWRtaW46YjkxNTA1YTQwN2I2NDBiNTgwZWNiNDc0NTE2OWU1YzE=";
    String basePath = "http://localhost:8080/";

    /*String getCrumbIssuer() {
        return given()
                .when().get(basePath + "crumbIssuer/api/json")
                .then().extract().path("crumb");
    }*/

    RequestSpecification authorizedGiven() {
        return given()
                .header("Authorization", authorizationHeader);
                //.header("Jenkins-Crumb", getCrumbIssuer());
    }

}
