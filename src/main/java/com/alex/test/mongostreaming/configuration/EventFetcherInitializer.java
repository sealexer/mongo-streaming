package com.alex.test.mongostreaming.configuration;

import com.alex.test.mongostreaming.consumer.EventFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("mongostreaming.consumer")
public class EventFetcherInitializer {

  private static final Logger log = LoggerFactory.getLogger(EventFetcherInitializer.class);

  private final EventFetcher eventFetcher;

  @Autowired
  public EventFetcherInitializer(EventFetcher eventFetcher) {
    this.eventFetcher = eventFetcher;
  }

  @EventListener(ContextRefreshedEvent.class)
  @Order(2)
  public void initialize() {
    log.info("Starting EventFetcher...");
    Thread thread = new Thread(eventFetcher, "event-fetching-thread");
    thread.start();
  }
}
