package com.example.springbatch.batch;

import  com.example.springbatch.dto.CoinMarket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class JsonJob2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 5; // 한번에 메모리에 가지고 올 청크 사이즈

    @Bean
    public Job jsonJob2_batchBuild(){
        return jobBuilderFactory.get("jsonJob2").start(jsonJob2_batchStep1()).build();
    }

    @Bean
    public Step jsonJob2_batchStep1(){
        return stepBuilderFactory.get("jsonJob2_batchStep1")
                .<CoinMarket, CoinMarket>chunk(chunkSize)
                .reader(jsonJob2_jsonReader())
                .processor(jsonJob2_processor())
                .writer(jsonJob2_jsonWriter()).build();
    }

    private ItemProcessor<CoinMarket, CoinMarket> jsonJob2_processor() {
        return coinMarket ->{
            if(!coinMarket.getMarket().startsWith("KRW-")){
                return null;
            }
            return new CoinMarket(coinMarket.getMarket(), coinMarket.getKorean_name(), coinMarket.getEnglish_name());
        };
    }

    @Bean
    public JsonItemReader<CoinMarket> jsonJob2_jsonReader(){
        return new JsonItemReaderBuilder<CoinMarket>() // 이런걸 만들겠다
                .jsonObjectReader(new JacksonJsonObjectReader<>(CoinMarket.class)) // 해당 DTO 객체 설정
                .resource(new ClassPathResource("sample/jsonJob2_input.json")) // 어느 파일로부터 가지고 올 것인가?
                .name("jsonJob2_jsonReader")
                .build();
    }

    @Bean
    public JsonFileItemWriter<CoinMarket> jsonJob2_jsonWriter(){
        return new JsonFileItemWriterBuilder<CoinMarket>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource("output/jsonJob2_output.json"))
                .name("jsonJob2_jsonWriter")
                .build();
    }
}