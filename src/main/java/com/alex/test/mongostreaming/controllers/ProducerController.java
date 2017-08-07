package com.alex.test.mongostreaming.controllers;

import com.alex.test.mongostreaming.model.Event;
import com.alex.test.mongostreaming.producer.EventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty("mongostreaming.producer")
public class ProducerController {

  private static final Logger log = LoggerFactory.getLogger(ProducerController.class);

  private final EventProducer eventProducer;

  @Autowired
  public ProducerController(EventProducer eventProducer) {
    this.eventProducer = eventProducer;
  }

  @RequestMapping(value = "produce", method = RequestMethod.GET)
  public String produce(@RequestParam("count") int count) {

    for (int i = 0; i < count; i++) {
      eventProducer.produce(new Event());
    }

    return "OK";
  }

}
