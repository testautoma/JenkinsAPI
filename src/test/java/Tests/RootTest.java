package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import Jenkins.JobDescriptor;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RootTest extends BaseTest {

    @Test
    public void requestWithoutAuthorization() {
        given()
                .when().get(network.path("api/json"))
                .then().statusCode(403);  //403 Forbidden
    }

    @Test
    public void getAllItems() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);
        jobNamesToDelete.add(job.name);
        Job job2 = JobBuilder.defaultValues().setName("Test2").build();
        network.createJob(job2);
        jobNamesToDelete.add(job2.name);

        List<JobDescriptor> descriptors = network.authorizedGiven()
                .when().get(network.path("api/json"))
                .then().statusCode(200)
                .extract().jsonPath().getList("jobs", JobDescriptor.class);

        Assert.assertEquals(2, descriptors.size());
    }
}
