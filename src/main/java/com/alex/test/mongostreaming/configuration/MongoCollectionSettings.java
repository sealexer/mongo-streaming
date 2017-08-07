package com.alex.test.mongostreaming.configuration;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@ToString
public class MongoCollectionSettings {

  @Value("${mongo-streaming.collection.name}")
  private String name;

  @Value("${mongo-streaming.collection.is-capped}")
  private Boolean isCapped;

  @Value("${mongo-streaming.collection.size.in-documents}")
  private Integer sizeInDocuments;

  @Value("${mongo-streaming.collection.size.in-bytes}")
  private Integer sizeInBytes;

}
