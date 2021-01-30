package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import org.junit.Assert;
import org.junit.Test;

public class CopyJob extends BaseTest {
    @Test
    public void copyItem() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        String job2Name = "Test2";
        network.createJob(job);

        network.authorizedGiven()
                .queryParam("name", job2Name)
                .queryParam("mode", "copy")
                .queryParam("from", job.name)
                .contentType("application/xml")
                .when().post(network.path("createItem"));
        Job job2 = Job.getJobFromResponse(network.getJob(job2Name));
        Assert.assertTrue(job.sameParameters(job2));

        network.deleteJob(job.name);
        network.deleteJob(job2Name);
    }

    @Test
    public void copyItemWithoutParameters() {
        Job job = JobBuilder.defaultValues().setName("Test1").build();
        network.createJob(job);

        network.authorizedGiven()
                .queryParam("from", job.name)
                .contentType("application/xml")
                .when().post(network.path("createItem"))
                .then().statusCode(400); //400 Bad Request

        network.deleteJob(job.name);
    }
}
