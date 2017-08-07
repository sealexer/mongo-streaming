package com.alex.test.mongostreaming.configuration;

import com.mongodb.DBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoInitializer {

  private static final Logger log = LoggerFactory.getLogger(MongoInitializer.class);

  private final MongoTemplate mongoTemplate;
  private final MongoCollectionSettings mongoCollectionSettings;

  @Autowired
  public MongoInitializer(MongoTemplate mongoTemplate,
      MongoCollectionSettings mongoCollectionSettings) {
    this.mongoTemplate = mongoTemplate;
    this.mongoCollectionSettings = mongoCollectionSettings;
  }

  @EventListener(ContextRefreshedEvent.class)
  @Order(1)
  public void initialize() {
    String collectionName = mongoCollectionSettings.getName();
    if (!mongoTemplate.collectionExists(collectionName)) {
      CollectionOptions options = new CollectionOptions(
          mongoCollectionSettings.getSizeInBytes(),
          mongoCollectionSettings.getSizeInDocuments(),
          mongoCollectionSettings.getIsCapped()
      );
      DBCollection collection = mongoTemplate.createCollection(collectionName, options);
      log.info("Created capped collection [settings={}]", mongoCollectionSettings);
    } else {
      DBCollection collection = mongoTemplate.getCollection(collectionName);
      log.info("Capped collection exists. Not recreating [name={}, isCapped={}]",
          collection.getFullName(),
          collection.isCapped());
    }
  }
}
