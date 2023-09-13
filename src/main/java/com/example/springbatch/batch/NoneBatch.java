package com.example.springbatch.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class NoneBatch {
    private final JobBuilderFactory jobBuilderFactory;
    @Bean
    public Job none_batchBuild(){
        return jobBuilderFactory.get("NONE")
                .start(none_batchStep1())
                .build();
    }

    @Bean
    public Step none_batchStep1(){
        return null;
    }
}
