package com.govtech.restaurantpicker.batch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.govtech.restaurantpicker.repository.UserRepository;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles({ "test", "batch" })
class UserBatchJobTest {

        @Autowired
        private JobLauncherTestUtils jobLauncherTestUtils;

        @Autowired
        private UserRepository userRepository;

        @Test
        void loadUsersJobTest() throws Exception {

                long beforeCount = userRepository.count();

                JobExecution execution = jobLauncherTestUtils.launchJob();

                assertThat(execution.getStatus())
                                .isEqualTo(BatchStatus.COMPLETED);

                long afterCount = userRepository.count();

                // CSV has 3 records
                assertThat(afterCount - beforeCount)
                                .isEqualTo(3);
        }

}
