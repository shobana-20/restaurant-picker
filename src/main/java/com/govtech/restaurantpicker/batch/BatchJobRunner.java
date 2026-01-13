package com.govtech.restaurantpicker.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * BatchJobRunner is responsible for triggering Spring Batch jobs
 * automatically when the application starts in the batch profile.
 *
 */
@Component
@Profile("batch")
public class BatchJobRunner {

    /** Launches Spring Batch jobs */
    private final JobLauncher jobLauncher;

    /** Batch job responsible for loading users */
    private final Job loadUsersJob;

    /**
     * Constructs a BatchJobRunner with required dependencies.
     *
     * @param jobLauncher  used to execute batch jobs
     * @param loadUsersJob the batch job that loads users into the system
     */
    public BatchJobRunner(JobLauncher jobLauncher, Job loadUsersJob) {
        this.jobLauncher = jobLauncher;
        this.loadUsersJob = loadUsersJob;
    }

    /**
     * Executes the batch job after the Spring context has been initialized.
     *
     * @throws Exception if the batch job execution fails
     */
    @PostConstruct
    public void runJob() throws Exception {
        jobLauncher.run(
                loadUsersJob,
                new JobParametersBuilder()
                        .addLong("run.id", System.currentTimeMillis())
                        .toJobParameters());
    }
}
