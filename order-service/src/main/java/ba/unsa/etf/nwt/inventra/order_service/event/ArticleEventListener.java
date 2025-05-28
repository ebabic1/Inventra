package ba.unsa.etf.nwt.inventra.order_service.event;

import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.service.ArticleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleEventListener {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "article.changed", durable = "true"),
            exchange = @Exchange(value = "inventra.exchange", type = "topic"),
            key = {"article.created", "article.updated", "article.deleted"}
    ))
    public void handleArticleEvent(Message message,
                                   @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        try {
            String jsonPayload = new String(message.getBody(), StandardCharsets.UTF_8);
            log.debug("Processing {} event: {}", routingKey, jsonPayload);

            ArticleDTO articleDTO = objectMapper.readValue(jsonPayload, ArticleDTO.class);

            switch (routingKey) {
                case "article.created":
                    articleService.create(articleMapper.toEntity(articleDTO));
                    break;
                case "article.updated":
                    articleService.update(articleDTO.getId(), articleMapper.toEntity(articleDTO));
                    break;
                case "article.deleted":
                    articleService.delete(articleDTO.getId());
                    break;
                default:
                    log.warn("Unknown routing key: {}", routingKey);
            }
        } catch (Exception e) {
            log.error("Failed to process {} event", routingKey, e);
            throw new AmqpRejectAndDontRequeueException("Processing failed", e);
        }
    }
}
