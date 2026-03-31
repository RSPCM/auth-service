package com.example.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // teacher rabbit mq config
    public static final String TEACHER_REGISTRATION_EXCHANGE = "teacher.register.exchange";
    public static final String TEACHER_REGISTER_QUEUE = "teacher.register.request.q";
    public static final String TEACHER_RK = "teacher.register";

    public static final String TEACHER_DLX = "dlx.teacher.register.exchange";
    public static final String TEACHER_DLQ_NAME = "teacher.register.error.q";
    public static final String TEACHER_DLX_RK = "teacher.register.error";


    @Bean
    public DirectExchange teacherRegistrationExchange() {
        return new DirectExchange(TEACHER_REGISTRATION_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange teacherRegistrationDeadLetterExchange() {
        return new DirectExchange(TEACHER_DLX, true, false);
    }

    @Bean
    public Queue teacherRegistrationRequestQueue() {
        return QueueBuilder.durable(TEACHER_REGISTER_QUEUE)
                .deadLetterExchange(TEACHER_DLX)
                .deadLetterRoutingKey(TEACHER_DLX_RK)
                .build();
    }

    @Bean
    public Queue teacherDeadLetterQueue() {
        return QueueBuilder.durable(TEACHER_DLQ_NAME)
                .build();
    }

    @Bean
    public Binding teacherRegistrationBinding() {
        return BindingBuilder.bind(teacherRegistrationRequestQueue())
                .to(teacherRegistrationExchange())
                .with(TEACHER_RK);
    }

    // Binding
    @Bean
    public Binding teacherDLQBinding() {
        return BindingBuilder.bind(teacherDeadLetterQueue())
                .to(teacherRegistrationDeadLetterExchange())
                .with(TEACHER_DLX_RK);
    }

    // ===== COMMON =====
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
/*
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }*/

    /*@Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }*/
    // ==========
}
