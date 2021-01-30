package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import org.junit.Assert;
import org.junit.Test;

public class CreateItemTest extends BaseTest{

    @Test
    public void createItem() {
        Job expectedJob = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(expectedJob)
                .then().statusCode(200);
        jobNamesToDelete.add(expectedJob.name);
        Job actualJob = Job.getJobFromResponse(network.getJob(expectedJob.name));

        Assert.assertTrue(expectedJob.same(actualJob));
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

    @Test
    public void copyItem() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);
        jobNamesToDelete.add(job.name);

        String job2Name = "Test2";

        network.authorizedGiven()
                .queryParam("name", job2Name)
                .queryParam("mode", "copy")
                .queryParam("from", job.name)
                .contentType("application/xml")
                .when().post(network.path("createItem"));
        jobNamesToDelete.add(job2Name);

        Job job2 = Job.getJobFromResponse(network.getJob(job2Name));

        Assert.assertTrue(job.sameParameters(job2));
    }

    @Test
    public void copyItemWithoutParameters() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);
        jobNamesToDelete.add(job.name);

        network.authorizedGiven()
                .queryParam("from", job.name)
                .contentType("application/xml")
                .when().post(network.path("createItem"))
                .then().statusCode(400); //400 Bad Request
    }
}
