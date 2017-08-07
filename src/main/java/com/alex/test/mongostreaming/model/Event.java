package com.alex.test.mongostreaming.model;

import lombok.Data;

@Data
public class Event {
  private long serverTime = System.currentTimeMillis();
}
