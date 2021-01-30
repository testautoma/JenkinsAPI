package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetJob extends BaseTest {

    @Test
    public void getExistedItem() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);

        Response response = network.getJob(job.name);
        response.then().statusCode(200);
        Job actualJob = Job.getJobFromResponse(response);
        Assert.assertEquals(job.name, actualJob.name);

        network.deleteJob(job.name);
    }

    @Test
    public void getAllItemsWithoutAuthorization() {
        given()
                .when().get(network.path("api/json"))
                .then().statusCode(403);  //403 Forbidden
    }

    @Test
    public void getNotExistedItem() {
        network.getJob("Test555").then().statusCode(404);   //404 Not Found
    }
}

