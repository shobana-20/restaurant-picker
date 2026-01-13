package com.govtech.restaurantpicker.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.govtech.restaurantpicker.entity.User;
import com.govtech.restaurantpicker.repository.UserRepository;

/**
 * Spring Batch configuration for loading users from a CSV file.
 *
 * This job is profile-based and runs only when the "batch" profile is active.
 * It reads users from users.csv and persists them into the database.
 */
@Profile("batch")
@Configuration
public class UserBatchConfig {

    @Bean
    public FlatFileItemReader<User> userReader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("users.csv"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("name");

        BeanWrapperFieldSetMapper<User> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(User.class);

        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public Step loadUsersStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            UserRepository userRepository) {

        return new StepBuilder("loadUsersStep", jobRepository)
                .<User, User>chunk(5, transactionManager)
                .reader(userReader())
                .writer(userRepository::saveAll)
                .build();
    }

    @Bean
    public Job loadUsersJob(
            JobRepository jobRepository,
            Step loadUsersStep) {

        return new JobBuilder("loadUsersJob", jobRepository)
                .start(loadUsersStep)
                .build();
    }
}