package com.example.springbatch.batch;

import com.example.springbatch.dto.OneDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TextJob1 { // TEXT 화일로부터 값 읽기
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 5; // 한번에 메모리에 가지고 올 청크 사이즈

    @Bean
    public Job textJob1_batchBuild(){
        return jobBuilderFactory.get("textJob1")
                .start(textJob1_batchStep1())
                .build();
    }

    @Bean
    public Step textJob1_batchStep1(){
        return stepBuilderFactory.get("textJob1_batchStep1")
                .<OneDto,OneDto>chunk(chunkSize)
                .reader(textJob1_FileReader())
                .writer(oneDto->oneDto.stream().forEach(i -> log.debug(i.toString())) )
                .build();
    }
    @Bean
    public FlatFileItemReader<OneDto> textJob1_FileReader(){ // TXT를 읽어서 리턴하겠다라는 의미
        FlatFileItemReader<OneDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/textJob1_input.txt"));
        flatFileItemReader.setLineMapper((line,lineNumber)-> new OneDto(lineNumber+"=="+line));
        return flatFileItemReader;
    }
}