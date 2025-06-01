package ba.unsa.etf.nwt.inventra.inventory_service.messaging.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("inventra.exchange");
    }

    @Bean
    public Queue inventoryFinishedQueue() {
        return new Queue("order.finished");
    }

    @Bean
    public Queue orderRollbackQueue() {
        return new Queue("order.rollback");
    }

    @Bean
    public Queue articleChangedQueue() {
        return new Queue("article.changed");
    }

    @Bean
    public Binding inventoryBinding() {
        return BindingBuilder.bind(inventoryFinishedQueue()).to(exchange()).with("order.finished");
    }

    @Bean
    public Binding rollbackBinding() {
        return BindingBuilder.bind(orderRollbackQueue()).to(exchange()).with("order.rollback");
    }

    @Bean
    public Binding articleCreatedBinding() {
        return BindingBuilder.bind(articleChangedQueue()).to(exchange()).with("article.created");
    }

    @Bean
    public Binding articleUpdatedBinding() {
        return BindingBuilder.bind(articleChangedQueue()).to(exchange()).with("article.updated");
    }

    @Bean
    public Binding articleDeletedBinding() {
        return BindingBuilder.bind(articleChangedQueue()).to(exchange()).with("article.deleted");
    }

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}

