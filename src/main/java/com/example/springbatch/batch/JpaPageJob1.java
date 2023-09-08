package com.example.springbatch.batch;

import com.example.springbatch.domain.Dept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob1 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private int chunkSize = 10; // 한번에 메모리에 가지고 올 청크 사이즈

    @Bean
    public Job JpaPageJob1_batchBuild(){
        return jobBuilderFactory.get("jpaPageJob1")
                .start(JpaPageJob1_step1()).build();
    }

    @Bean
    public Step JpaPageJob1_step1(){
        return stepBuilderFactory.get("jpaPageJob1_step1")
                .<Dept,Dept>chunk(chunkSize) // 내가 읽어서 아웃으로 보낸다
                .reader(jpaPageJob1_dbItemReader())
                .writer(jpaPageJob1_printItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob1_dbItemReader(){
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob1_dbItemReader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("SELECT d FROM Dept d order by dept_no asc")
                .build();
    }

    @Bean
    public ItemWriter<Dept> jpaPageJob1_printItemWriter(){
        return list -> {
            for(Dept dept : list){
                log.debug(dept.toString());
            }
        };
    }


}