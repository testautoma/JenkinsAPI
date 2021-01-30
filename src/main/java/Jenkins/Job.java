package Jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.restassured.response.Response;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    public String name;
    public String url;
    public Boolean disabled;

    public static Job getJobFromResponse(Response response) {
        return response.then().extract().jsonPath().getObject("", Job.class);
    }

    public String xmlConfigString() {
        return "<project><disabled>" + disabled + "</disabled></project>";
    }

    public Boolean same(Job job) {
        return name.equals(job.name) &&
                sameParameters(job);
    }

    public Boolean sameParameters(Job job) {
        return disabled.equals(job.disabled);
    }
}
