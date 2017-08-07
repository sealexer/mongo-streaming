package com.alex.test.mongostreaming.producer.impl;

import com.alex.test.mongostreaming.configuration.MongoCollectionSettings;
import com.alex.test.mongostreaming.model.Event;
import com.alex.test.mongostreaming.producer.EventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducerImpl implements EventProducer {

  private static final Logger log = LoggerFactory.getLogger(EventProducerImpl.class);

  private final MongoTemplate mongoTemplate;
  private final MongoCollectionSettings mongoCollectionSettings;

  @Autowired
  public EventProducerImpl(MongoTemplate mongoTemplate,
      MongoCollectionSettings mongoCollectionSettings) {
    this.mongoTemplate = mongoTemplate;
    this.mongoCollectionSettings = mongoCollectionSettings;
  }

  @Override
  public void produce(Event event) {
    log.debug("Sending to mongo: {}", event);
    mongoTemplate.save(event, mongoCollectionSettings.getName());
  }

}
