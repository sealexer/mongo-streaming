package com.alex.test.mongostreaming.consumer;


import static com.mongodb.Bytes.QUERYOPTION_AWAITDATA;
import static com.mongodb.Bytes.QUERYOPTION_TAILABLE;

import com.alex.test.mongostreaming.configuration.MongoCollectionSettings;
import com.alex.test.mongostreaming.configuration.ServiceCollectionSettings;
import com.alex.test.mongostreaming.model.Event;
import com.alex.test.mongostreaming.model.ServiceInfo;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class EventFetcher implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(EventFetcher.class);

  private final MongoTemplate mongoTemplate;
  private final MongoCollectionSettings mongoCollectionSettings;
  private final ServiceCollectionSettings serviceCollectionSettings;
  private final EventConsumer eventConsumer;

  private ServiceInfo serviceInfo;

  @Autowired
  public EventFetcher(
      MongoTemplate mongoTemplate,
      MongoCollectionSettings mongoCollectionSettings,
      ServiceCollectionSettings serviceCollectionSettings,
      EventConsumer eventConsumer) {
    this.mongoTemplate = mongoTemplate;
    this.mongoCollectionSettings = mongoCollectionSettings;
    this.serviceCollectionSettings = serviceCollectionSettings;
    this.eventConsumer = eventConsumer;
  }

  @Override
  public void run() {
    while (true) {
      DBCursor cursor = buildTailableCursor();
      while (cursor.hasNext()) {
        DBObject eventObject = cursor.next();
        Event event = mongoTemplate.getConverter().read(Event.class, eventObject);
        eventConsumer.consume(event);
        setLastSeenEventTime(event.getServerTime());
      }
      // We can get here e.g. if first hasNext() invoked a query, which returned an empty result
      // See https://docs.mongodb.com/manual/core/tailable-cursors
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        log.warn("Thread.sleep interrupted", e);
      }
    }
  }

  private void setLastSeenEventTime(long time) {
    getServiceInfo().setLastSeenEventTime(time);
    persistServiceInfo();
  }

  private void persistServiceInfo() {
    mongoTemplate.save(getServiceInfo(), serviceCollectionSettings.getName());
  }

  private Long getLastSeenEventTime() {
    return getServiceInfo().getLastSeenEventTime();
  }

  private ServiceInfo getServiceInfo() {
    if (serviceInfo == null) {
      serviceInfo = mongoTemplate
          .findOne(new Query(), ServiceInfo.class, serviceCollectionSettings.getName());
      if (serviceInfo == null) {
          serviceInfo = new ServiceInfo();
      }
    }
    return serviceInfo;
  }

  private DBCursor buildTailableCursor() {
    DBCollection collection = mongoTemplate.getCollection(mongoCollectionSettings.getName());
    return collection
        .find(buildQueryParam())
        .addOption(QUERYOPTION_TAILABLE)
        .addOption(QUERYOPTION_AWAITDATA);
  }

  private DBObject buildQueryParam() {
    Long lastSeenEventTime = getLastSeenEventTime();
    return lastSeenEventTime == null
        ? null
        : QueryBuilder.start("serverTime").greaterThan(lastSeenEventTime).get();
  }


}
