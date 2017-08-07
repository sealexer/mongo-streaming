package com.alex.test.mongostreaming.consumer.impl;

import com.alex.test.mongostreaming.model.Event;
import com.alex.test.mongostreaming.consumer.EventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventConsumerImpl implements EventConsumer {

  private static final Logger log = LoggerFactory.getLogger(EventConsumerImpl.class);

  @Override
  public void consume(Event event) {
    log.debug("Event consumed: {}", event);
  }

}
