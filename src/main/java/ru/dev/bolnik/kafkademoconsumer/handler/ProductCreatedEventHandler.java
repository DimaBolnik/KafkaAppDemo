package ru.dev.bolnik.kafkademoconsumer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.dev.bolnik.core.ProductCreatedEvent;
import ru.dev.bolnik.kafkademoconsumer.exception.NonRetryableException;
import ru.dev.bolnik.kafkademoconsumer.exception.RetryableException;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
//        if (true)
//            throw new NonRetryableException("non-retryable exception");
        LOGGER.info("Received event: {}", productCreatedEvent.getTitle());
        String url = "http://localhost:8090/response/500" + productCreatedEvent.getProductId();

        try {
            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (exchange.getStatusCode().value() == HttpStatus.OK.value()) {
                LOGGER.info("Received event: {}", exchange.getBody());
            }
        } catch (ResourceAccessException e) {
           LOGGER.error(e.getMessage());
           throw new RetryableException(e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NonRetryableException(e);
        }


    }


}
