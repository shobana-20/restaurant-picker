package com.govtech.restaurantpicker.batch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.govtech.restaurantpicker.repository.UserRepository;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles({"test", "batch"})
class UserBatchJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    @Test
    void loadUsersJob_shouldInsertUsers() throws Exception {

        JobExecution execution =
                jobLauncherTestUtils.launchJob();

        assertThat(execution.getStatus())
                .isEqualTo(BatchStatus.COMPLETED);

        assertThat(userRepository.count())
                .isGreaterThan(0);
    }
}
