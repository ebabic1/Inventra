package ba.unsa.etf.nwt.inventra.order_service.messaging.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "inventra.exchange";

    public static final String ARTICLE_QUEUE = "article.changed.queue";
    public static final String ARTICLE_CREATED_KEY = "article.created";
    public static final String ARTICLE_UPDATED_KEY = "article.updated";
    public static final String ARTICLE_DELETED_KEY = "article.deleted";

    public static final String ORDER_FINISHED_QUEUE = "order.finished";
    public static final String ORDER_ROLLBACK_QUEUE = "order.rollback";
    public static final String ORDER_FINISHED_KEY = "order.finished";
    public static final String ORDER_ROLLBACK_KEY = "order.rollback";

    @Bean
    public TopicExchange inventraExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }


    @Bean
    public Queue articleChangedQueue() {
        return QueueBuilder.durable(ARTICLE_QUEUE).build();
    }

    @Bean
    public Queue orderFinishedQueue() {
        return QueueBuilder.durable(ORDER_FINISHED_QUEUE).build();
    }

    @Bean
    public Queue orderRollbackQueue() {
        return QueueBuilder.durable(ORDER_ROLLBACK_QUEUE).build();
    }

    @Bean
    public Binding articleCreatedBinding() {
        return BindingBuilder.bind(articleChangedQueue())
                .to(inventraExchange())
                .with(ARTICLE_CREATED_KEY);
    }

    @Bean
    public Binding articleUpdatedBinding() {
        return BindingBuilder.bind(articleChangedQueue())
                .to(inventraExchange())
                .with(ARTICLE_UPDATED_KEY);
    }

    @Bean
    public Binding articleDeletedBinding() {
        return BindingBuilder.bind(articleChangedQueue())
                .to(inventraExchange())
                .with(ARTICLE_DELETED_KEY);
    }

    @Bean
    public Binding orderFinishedBinding() {
        return BindingBuilder.bind(orderFinishedQueue())
                .to(inventraExchange())
                .with(ORDER_FINISHED_KEY);
    }

    @Bean
    public Binding orderRollbackBinding() {
        return BindingBuilder.bind(orderRollbackQueue())
                .to(inventraExchange())
                .with(ORDER_ROLLBACK_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
