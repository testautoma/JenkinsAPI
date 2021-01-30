package Jenkins;

import io.restassured.http.Cookies;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Base64;

import static io.restassured.RestAssured.given;

public class Network {

    private String basePath;
    private String user;
    private String password;

    private Cookies cookies = new Cookies();

    public Network(String basePath, String user, String password) {
        this.basePath = basePath;
        this.user = user;
        this.password = password;
    }

    private String authorizationHeader() {
        String auth = user + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private String getCrumbIssuer() {
        ExtractableResponse response = given()
                .header("Authorization", authorizationHeader())
                .when().get(basePath + "crumbIssuer/api/json")
                .then().extract();

        cookies = response.response().getDetailedCookies();

        return response.path("crumb");
    }

    public RequestSpecification authorizedGiven() {
        String crumb = getCrumbIssuer();
        return given()
                .cookies(cookies)
                .header("Authorization", authorizationHeader())
                .header("Jenkins-Crumb", crumb);
    }

    public Response createJob(Job job) {
        return authorizedGiven()
                .body(job.xmlConfigString())
                .contentType("application/xml")
                .queryParam("name", job.name)
                .when().post(basePath + "createItem/");
    }

    public Response getJob(String name) {
        return authorizedGiven()
                .when().get(basePath + "job/" + name + "/api/json");
    }

    public Response deleteJob(String name) {
        return authorizedGiven()
                .when().post(basePath + "job/" + name + "/doDelete");
    }

    public Response disableJob(String name) {
        return authorizedGiven()
                .when().post(basePath + "job/" + name + "/disable");
    }

    public Response enableJob(String name) {
        return authorizedGiven()
                .when().post(basePath + "job/" + name + "/enable");
    }

    public String path(String relativePath) {
        return basePath + relativePath;
    }
}
