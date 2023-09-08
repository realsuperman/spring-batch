package com.example.springbatch.batch;

import  com.example.springbatch.dto.CoinMarket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class JsonJob2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 5; // 한번에 메모리에 가지고 올 청크 사이즈

    @Bean
    public Job jsonJob1_batchBuild(){
        return jobBuilderFactory.get("jsonJob1").start(jsonJob1_batchStep1()).build();
    }

    @Bean
    public Step jsonJob1_batchStep1(){
        return stepBuilderFactory.get("jsonJob1_batchStep1")
                .<CoinMarket, CoinMarket>chunk(chunkSize)
                .reader(jsonJob1_jsonReader())
                .writer(coinMarkets -> coinMarkets.stream().forEach(coinMarkets2 -> log.debug(coinMarkets2.toString()))).build();
    }

    @Bean
    public JsonItemReader<CoinMarket> jsonJob1_jsonReader(){
        return new JsonItemReaderBuilder<CoinMarket>() // 이런걸 만들겠다
                .jsonObjectReader(new JacksonJsonObjectReader<>(CoinMarket.class)) // 해당 DTO 객체 설정
                .resource(new ClassPathResource("sample/jsonJob1_input.json")) // 어느 파일로부터 가지고 올 것인가?
                .name("jsonJob1_jsonReader")
                .build();
    }
}