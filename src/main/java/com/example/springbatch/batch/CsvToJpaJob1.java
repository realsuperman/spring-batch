package com.example.springbatch.batch;

import com.example.springbatch.domain.Dept;
import com.example.springbatch.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CsvToJpaJob1 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private int chunkSize = 10;

    @Bean
    public Job csvToJpaJob1_batchBuild() {
        return jobBuilderFactory.get("csvToJpaJob1")
                .start(csvToJpaJob1_batchStep1())
                .build();
    }

    @Bean
    public Step csvToJpaJob1_batchStep1(){
        return stepBuilderFactory.get("csvToJpaJob1_batchStep1")
                .<TwoDto, Dept> chunk(chunkSize)
                .reader(csvToJpaJob1_FileReader())
                .processor(csvToJpaJob1_processor())
                .writer(csvToJpaJob1_dbItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<TwoDto> csvToJpaJob1_FileReader() {
        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/csvToJpaJob1_input.csv"));

        DefaultLineMapper<TwoDto> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("one","two");
        delimitedLineTokenizer.setDelimiter(":");

        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class);
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

    @Bean
    public ItemProcessor<TwoDto, Dept> csvToJpaJob1_processor(){ // input에서 읽은걸 한 줄 한 줄 읽어서 Dept 타입으로 변환
        return twoDto -> new Dept(Integer.parseInt(twoDto.getOne()), twoDto.getTwo(), "기타");
    }

    @Bean
    public JpaItemWriter<Dept> csvToJpaJob1_dbItemWriter(){
        JpaItemWriter<Dept> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(emf);
        return jpaItemWriter;
    }

}