package com.alex.test.mongostreaming.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceCollectionSettings {

  @Value("${mongo-streaming.service-collection.name}")
  private String name;

}
