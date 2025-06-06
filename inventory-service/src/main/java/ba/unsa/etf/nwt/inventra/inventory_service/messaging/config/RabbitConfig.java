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
    public static final String EXCHANGE_NAME = "inventra.exchange";

    public static final String ORDER_FINISHED_QUEUE = "order.finished";
    public static final String ORDER_ROLLBACK_QUEUE = "order.rollback";
    public static final String ARTICLE_CHANGED_QUEUE = "order.article.changed";

    public static final String ROUTING_KEY_ORDER_FINISHED = "order.finished";
    public static final String ROUTING_KEY_ORDER_ROLLBACK = "order.rollback";
    public static final String ROUTING_KEY_ARTICLE_CREATED = "article.created";
    public static final String ROUTING_KEY_ARTICLE_UPDATED = "article.updated";
    public static final String ROUTING_KEY_ARTICLE_DELETED = "article.deleted";

    public static final String LOW_STOCK_QUEUE = "notification.lowstock";
    public static final String LOW_STOCK_ROUTING_KEY = "notification.lowstock";

    public static final String EXPIRY_DATE_QUEUE = "notification.expirydate";
    public static final String EXPIRY_DATE_ROUTING_KEY = "notification.expirydate";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue inventoryFinishedQueue() {
        return new Queue(ORDER_FINISHED_QUEUE);
    }

    @Bean
    public Queue orderRollbackQueue() {
        return new Queue(ORDER_ROLLBACK_QUEUE);
    }

    @Bean
    public Queue articleChangedQueue() {
        return new Queue(ARTICLE_CHANGED_QUEUE);
    }

    @Bean
    public Queue lowStockQueue() {
        return new Queue(LOW_STOCK_QUEUE);
    }

    @Bean
    public Queue expiryDateQueue() {
        return new Queue(EXPIRY_DATE_QUEUE);
    }

    @Bean
    public Binding inventoryBinding() {
        return BindingBuilder.bind(inventoryFinishedQueue()).to(exchange()).with(ROUTING_KEY_ORDER_FINISHED);
    }

    @Bean
    public Binding rollbackBinding() {
        return BindingBuilder.bind(orderRollbackQueue()).to(exchange()).with(ROUTING_KEY_ORDER_ROLLBACK);
    }

    @Bean
    public Binding orderArticleCreatedBinding() {
        return BindingBuilder.bind(articleChangedQueue()).to(exchange()).with(ROUTING_KEY_ARTICLE_CREATED);
    }

    @Bean
    public Binding orderArticleUpdatedBinding() {
        return BindingBuilder.bind(articleChangedQueue()).to(exchange()).with(ROUTING_KEY_ARTICLE_UPDATED);
    }

    @Bean
    public Binding orderArticleDeletedBinding() {
        return BindingBuilder.bind(articleChangedQueue()).to(exchange()).with(ROUTING_KEY_ARTICLE_DELETED);
    }

    @Bean
    public Binding lowStockBinding() {
        return BindingBuilder.bind(lowStockQueue()).to(exchange()).with(LOW_STOCK_ROUTING_KEY);
    }

    @Bean
    public Binding expiryDateBinding() {
        return BindingBuilder.bind(expiryDateQueue()).to(exchange()).with(EXPIRY_DATE_ROUTING_KEY);
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

