package com.github.ricardorv.sincroniareceita;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Writer;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Override
    public void setDataSource(DataSource dataSource) {
    }

    @Autowired
    ApplicationArguments appArgs;

    @Bean
    public FlatFileItemReader<Conta> reader() {
        Resource resource = appArgs.getNonOptionArgs().size() > 0
                ? new PathResource(appArgs.getNonOptionArgs().get(0))
                : new ClassPathResource("contas.csv");

        return new FlatFileItemReaderBuilder<Conta>()
                .name("contaReader")
                .resource(resource)
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names(new String[]{"agencia", "conta", "saldo", "status"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Conta>() {{
                    setTargetType(Conta.class);
                }})
                .build();
    }

    @Bean
    public ContaProcessor processor() {
        return new ContaProcessor();
    }

    @Bean
    public FlatFileItemWriter<Conta> writer() {
        Resource resource = appArgs.getNonOptionArgs().size() > 1
                ? new PathResource(appArgs.getNonOptionArgs().get(1))
                : new ClassPathResource("out.csv");

        FlatFileItemWriter<Conta> writer = new FlatFileItemWriter<>();
        writer.setResource(resource);
        writer.setAppendAllowed(true);
        writer.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write("agencia;conta;saldo;status;resultado");
            }
        });
        writer.setLineAggregator(new DelimitedLineAggregator<Conta>() {
            {
                setDelimiter(";");
                setFieldExtractor(new BeanWrapperFieldExtractor<Conta>() {
                    {
                        setNames(new String[] { "agencia", "conta", "saldo", "status", "resultado" });
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public Job contaJob(Step step) {
        return jobBuilderFactory.get("contaJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(FlatFileItemWriter<Conta> writer) {
        return stepBuilderFactory.get("step1")
                .<Conta, Conta> chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .faultTolerant()
                .retryLimit(3)
                .retry(RuntimeException.class)
                .build();
    }

}
