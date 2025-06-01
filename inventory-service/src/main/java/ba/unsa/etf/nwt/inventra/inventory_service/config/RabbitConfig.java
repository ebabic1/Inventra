package ba.unsa.etf.nwt.inventra.inventory_service.config;

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

    public static final String EXCHANGE_NAME = "inventra.exchange";
    public static final String LOW_STOCK_QUEUE = "notification.lowstock";
    public static final String LOW_STOCK_ROUTING_KEY = "notification.lowstock";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
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
    public Queue lowStockQueue() {
        return new Queue(LOW_STOCK_QUEUE);
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
    public Binding lowStockBinding() {
        return BindingBuilder.bind(lowStockQueue()).to(exchange()).with(LOW_STOCK_ROUTING_KEY);
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
