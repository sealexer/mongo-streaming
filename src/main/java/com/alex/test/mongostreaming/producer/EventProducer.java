package com.alex.test.mongostreaming.producer;

import com.alex.test.mongostreaming.model.Event;

public interface EventProducer {

  void produce(Event event);

}
