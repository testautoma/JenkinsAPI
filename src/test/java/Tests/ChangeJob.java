package Tests;

import Jenkins.Job;
import Jenkins.JobBuilder;
import org.junit.Assert;
import org.junit.Test;

public class ChangeJob extends BaseTest {
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
}
