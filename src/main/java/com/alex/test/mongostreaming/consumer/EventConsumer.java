package com.alex.test.mongostreaming.consumer;

import com.alex.test.mongostreaming.model.Event;

public interface EventConsumer {

  void consume(Event event);
}
