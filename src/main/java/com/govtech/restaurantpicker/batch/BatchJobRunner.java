package com.govtech.restaurantpicker.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Profile("batch")
public class BatchJobRunner {

    private final JobLauncher jobLauncher;
    private final Job loadUsersJob;

    public BatchJobRunner(JobLauncher jobLauncher, Job loadUsersJob) {
        this.jobLauncher = jobLauncher;
        this.loadUsersJob = loadUsersJob;
    }

    @PostConstruct
    public void runJob() throws Exception {
        jobLauncher.run(
                loadUsersJob,
                new JobParametersBuilder()
                        .addLong("run.id", System.currentTimeMillis())
                        .toJobParameters()
        );
    }
}
