package com.example.springbatch.batch;

import com.example.springbatch.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class CsvJob1 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 5; // 한번에 메모리에 가지고 올 청크 사이즈

    @Bean
    public Job csvJob1_batchBuild(){
        return jobBuilderFactory.get("csvJob1")
                .start(csvJob1_batchStep1())
                .build();
    }

    @Bean
    public Step csvJob1_batchStep1(){
        return stepBuilderFactory.get("csvJob1_batchStep1")
                .<TwoDto, TwoDto>chunk(chunkSize)
                .reader(csvJob1_FileReader())
                .writer(twoDto -> twoDto.stream().forEach(twoDto2 -> log.debug(twoDto2.toString()))).build();
    }

    @Bean
    public FlatFileItemReader<TwoDto> csvJob1_FileReader(){
        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/csvJob1_input.csv"));
        flatFileItemReader.setLinesToSkip(1); // csv의 첫 라인은 헤더이므로 해당 라인은 패스

        DefaultLineMapper<TwoDto> dtoDefaultLineMapper = new DefaultLineMapper<>();
        
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(); // 딜리미터 관련
        delimitedLineTokenizer.setNames("one","two"); // 하나의 라인에 각각 몇개로 세팅할건가?(지금은 one:two 이므로 이렇게)
        delimitedLineTokenizer.setDelimiter(":"); // 구분자 설정

        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>(); // 타입 관련
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class); // 해당 클래스 타입으로 만들어라

        dtoDefaultLineMapper.setLineTokenizer(delimitedLineTokenizer); // 딜리미터 설정
        dtoDefaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper); // 타입설정
        
        flatFileItemReader.setLineMapper(dtoDefaultLineMapper); // 모든게 설정된 항목을 reader에 설정

        return flatFileItemReader;
    }
}
