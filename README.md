# mongo-streaming

#### Test-driving the streamlike reading from mongoDB

```
  This is a simple prototype consisting of a producer (that writes objects to mongoDB) 
and a consumer (that reads objects from mongoDB).
  The most important thing is that the consumer does not keep polling mongoDB, but simply
loops iterating over data, freezing when no more data is available and waking up as soon
as new data arrives.
  The trick is based on capped collections[*] and tailable cursors[**].

[*] https://docs.mongodb.com/manual/core/capped-collections/
[**] https://docs.mongodb.com/manual/core/tailable-cursors/   
```

* Run mongoDB at `localhost:27017` (or provide `MONGO_URI` via env var)
* Run a producer: `java -Dmongostreaming.producer -Dserver.port=8080 -jar target/mongo-streaming-1.0-SNAPSHOT.jar`
* Run a consumer: `java -Dmongostreaming.consumer -Dserver.port=8081 -jar target/mongo-streaming-1.0-SNAPSHOT.jar`
* Initiate objects emission on producer: [http://localhost:8080/produce?count=10](http://localhost:8080/produce?count=10)
* Observe consumer immediately receiving objects (from consumer logs) 