package Jenkins;

public class JobBuilder {
    private Job job;

    public JobBuilder() {
        job = new Job();
        job.name = "";
        job.url = "";
        job.disabled = false;
    }

    public static JobBuilder defaultValues() {
        return new JobBuilder();
    }

    public JobBuilder setName(String name) {
        job.name = name;
        return this;
    }

    public JobBuilder setUrl(String url) {
        job.url = url;
        return this;
    }

    public JobBuilder setDisabled(Boolean disabled) {
        job.disabled = disabled;
        return this;
    }

    public Job build() {
        return job;
    }
}
