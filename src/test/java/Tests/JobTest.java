package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class JobTest extends BaseTest{
    @Test
    public void disableItem() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);
        network.disableJob(job.name);
        job.disabled = true;
        Job actualJob = Job.getJobFromResponse(network.getJob(job.name));

        Assert.assertTrue(job.same(actualJob));

        network.deleteJob(job.name);
    }

    @Test
    public void enableItem() {
        Job job = JobBuilder.defaultValues()
                .setName("Test1")
                .setDisabled(true)
                .build();
        network.createJob(job);
        network.enableJob(job.name);
        job.disabled = false;
        Job actualJob = Job.getJobFromResponse(network.getJob(job.name));

        Assert.assertTrue(job.same(actualJob));

        network.deleteJob(job.name);
    }

    @Test
    public void deleteItem() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);
        Job job2 = JobBuilder.defaultValues().setName("Test2").build();
        network.createJob(job2);
        network.deleteJob(job.name);
        network.getJob(job.name)
                .then().statusCode(404);
        network.getJob(job2.name)
                .then().statusCode(200);

        network.deleteJob(job2.name);
    }

    @Test
    public void deleteNotExistedItem() {
        network.deleteJob("Test555")
                .then().statusCode(404);   //404 Not Found
    }


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
    public void getNotExistedItem() {
        network.getJob("Test555").then().statusCode(404);   //404 Not Found
    }
}
