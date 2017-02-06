package org.wefine;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;


@EnableBatchProcessing
@SpringBootApplication
public class Application {

    public static class Person {
        private int age;
        private String firstName, email;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Bean
    FlatFileItemReader<Person> itemReader() throws Exception {
        return new FlatFileItemReaderBuilder<Person>()
                .name("file-reader")
                .resource(new ClassPathResource("person.csv"))
                .targetType(Person.class)
                .delimited().delimiter(", ").names(new String[]{"age", "firstName", "email"})
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Person> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .dataSource(dataSource)
                .sql("insert into person(age, first_name, email) values (:age, :firstName, :email)")
                .beanMapped()
                .build();
    }

    @Bean
    Job job(JobBuilderFactory jbf,
            StepBuilderFactory sbf,
            ItemWriter<? super Person> iw,
            ItemReader<? extends Person> ir) {

        Step s1 = sbf.get("file-db")
                .<Person, Person>chunk(100)
                .reader(ir)
                .writer(iw)
                .build();

        return jbf.get("etl")
                .incrementer(new RunIdIncrementer())
                .start(s1)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
