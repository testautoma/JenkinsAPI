package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import Jenkins.JobDescriptor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GetAllItems extends BaseTest{

    Job job = JobBuilder.defaultValues().setName("Test1").build();
    Job job2 = JobBuilder.defaultValues().setName("Test2").build();

    @Before
    public void start() {
        network.createJob(job);
        network.createJob(job2);
    }

    @After
    public void finish() {
        network.deleteJob(job.name);
        network.deleteJob(job2.name);
    }

    @Test
    public void getAllItems() {
        List<JobDescriptor> descriptors = network.authorizedGiven()
                .when().get(network.path("api/json"))
                .then().statusCode(200)
                .extract().jsonPath().getList("jobs", JobDescriptor.class);
        Assert.assertEquals(2, descriptors.size());
    }
}
