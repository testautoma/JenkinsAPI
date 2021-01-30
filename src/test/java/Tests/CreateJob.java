package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import org.junit.Assert;
import org.junit.Test;

public class CreateJob extends BaseTest {
    @Test
    public void createItem() {
        Job expectedJob = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(expectedJob)
                .then().statusCode(200);
        Job actualJob = Job.getJobFromResponse(network.getJob(expectedJob.name));

        Assert.assertTrue(expectedJob.same(actualJob));

        network.deleteJob(expectedJob.name);
    }

    @Test
    public void createItemWithoutBody() {
        network.authorizedGiven()
                .contentType("application/xml")
                .queryParam("name", "Test3")
                .when().post(network.path("createItem"))
                .then().statusCode(500); //500 Server Error
    }

    @Test
    public void createItemWithoutName() {
        Job job = new Job();
        network.authorizedGiven()
                .body(job.xmlConfigString())
                .contentType("application/xml")
                .when().post(network.path("createItem"))
                .then().statusCode(400); //400 Bad Request
    }
}
