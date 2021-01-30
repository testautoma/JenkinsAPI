package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import org.junit.Test;

public class DeleteJob extends BaseTest {
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
}
