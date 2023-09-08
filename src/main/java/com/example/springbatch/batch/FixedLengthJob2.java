//package com.example.springbatch.batch;
//
//import com.example.springbatch.dto.TwoDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//
//@RequiredArgsConstructor
//@Slf4j
//@Configuration
//public class FixedLengthJob2 {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private int chunkSize = 5; // 한번에 메모리에 가지고 올 청크 사이즈
//
//    @Bean
//    public Job fixedLengthJob2_batchBuild(){
//        return jobBuilderFactory.get("fixedLengthJob2")
//                .start(fixedLengthJob2_batchStep1())
//                .build();
//    }
//
//    @Bean
//    public Step fixedLengthJob2_batchStep1(){
//        return stepBuilderFactory.get("fixedLengthJob2_batchStep1")
//                .<TwoDto, TwoDto>chunk(chunkSize)
//                .reader(fixedLengthJob2_FileReader())
//                .writer(fixedLengthJob2_fileWriter(new FileSystemResource("output/fixedLengthJob2_output.txt"))).build();
//    }
//
//    @Bean
//    public FlatFileItemReader<TwoDto> fixedLengthJob2_FileReader(){
//        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
//        flatFileItemReader.setResource(new ClassPathResource("/sample/fixedLengthJob2_input.txt"));
//        flatFileItemReader.setLinesToSkip(1); // csv의 첫 라인은 헤더이므로 해당 라인은 패스
//
//        DefaultLineMapper<TwoDto> dtoDefaultLineMapper = new DefaultLineMapper<>();
//
//        FixedLengthTokenizer fixedLengthTokenizer = new FixedLengthTokenizer();
//        fixedLengthTokenizer.setNames("one","two");
//        fixedLengthTokenizer.setColumns(new Range(1,5), new Range(6,10));
//
//        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>(); // 타입 관련
//        beanWrapperFieldSetMapper.setTargetType(TwoDto.class); // 해당 클래스 타입으로 만들어라
//
//        dtoDefaultLineMapper.setLineTokenizer(fixedLengthTokenizer); // 딜리미터 설정
//        dtoDefaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper); // 타입설정
//
//        flatFileItemReader.setLineMapper(dtoDefaultLineMapper); // 모든게 설정된 항목을 reader에 설정
//        return flatFileItemReader;
//    }
//
//    @Bean
//    public FlatFileItemWriter<TwoDto> fixedLengthJob2_fileWriter(Resource resource){
//        BeanWrapperFieldExtractor<TwoDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
//        fieldExtractor.setNames(new String[]{"one","two"}); // 필드를 두 개로 나눈다
//        fieldExtractor.afterPropertiesSet();
//
//        FormatterLineAggregator<TwoDto> lineAggregator = new FormatterLineAggregator<>();
//        lineAggregator.setFormat("%-5s###%5s"); // 특정 포맷 지정
//        lineAggregator.setFieldExtractor(fieldExtractor); // 뽑아낸다
//
//        return new FlatFileItemWriterBuilder<TwoDto>()
//                .name("fixedLengthJob2_FileWriter")
//                .resource(resource)
//                .lineAggregator(lineAggregator)
//                .build();
//    }
//}
