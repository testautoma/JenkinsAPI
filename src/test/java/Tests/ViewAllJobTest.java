package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ViewAllJobTest extends BaseTest {
    @Test
    public void jsonSchemaValidation() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);
        jobNamesToDelete.add(job.name);

        network.authorizedGiven()
                .when().get(network.path("view/all/job/" + job.name + "/api/json"))
                .then().assertThat().body(matchesJsonSchemaInClasspath("json-schema.json"));
    }
}
