package com.ltb.customerwebsite.configuration;

import com.ltb.customerwebsite.models.Customer;
import com.ltb.customerwebsite.repositories.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Map;

@Configuration
public class BatchConfig {

    @Bean
    public Job job(
            JobRepository jobRepository,
            Step readStep) {

        return new JobBuilder("customer-loader-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(readStep)
                .build();
    }

    @Bean
    public Step readStep(
            JobRepository jobRepository,
            ItemReader<Customer> csvReader,
            ItemWriter<Customer> writer,
            PlatformTransactionManager transactionManager) {

        return new StepBuilder("read-step", jobRepository)
                .<Customer, Customer>chunk(100, transactionManager)
                .reader(csvReader)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Customer> csvReader(
            @Value("${inputFile}") String inputFile) {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("csv-reader")
                .resource(new ClassPathResource(inputFile))
                .delimited()
                .names("fullName", "emailAddress", "age", "address")
                .linesToSkip(1)
                .fieldSetMapper(
                        new BeanWrapperFieldSetMapper<>() {
                            {setTargetType(Customer.class);}
                        })
                .build();
    }

    @Bean
    public ItemWriter<Customer> writer(CustomerRepository customerRepository) {
        return customerRepository::saveAll;
    }

}


