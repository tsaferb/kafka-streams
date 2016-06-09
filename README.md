#kafka-streams-drools
A fork of kafka-streams project to integrate Drools rule engine for Kafka Stream processing

 *   Kafka Streams - KStreams API with Drools Rule Processor
 
 The rest is the same.
 This is the repository for the examples of using Kafka streams covered in the blog posts:
 *   [Kafka Streams - The Processor API](http://codingjunkie.net/kafka-processor-part1/)
 *   [Kafka Streams - The KStreams API](http://codingjunkie.net/kafka-streams-part2/)
 *   [Machine Learning with Kafka Streams](http://codingjunkie.net/kafka-streams-machine-learning/)


## Requirements to build this project

1.    Java 8
2.    Gradle

## Requirements to run the examples

1.    [kafka](https://github.com/apache/kafka) version kafka_2.11-0.10.1.0-SNAPSHOT see the section marked "Running a task on a particular version of Scala"
2.    The [json-data-generator](https://github.com/acesinc/json-data-generator) from [ACES,Inc](http://acesinc.net/)


## Dependencies included in repository
As the Kafka Streams/Processor API is a work in progress, this repo includes the following jar files known to work with the examples

1.    kafka-streams-0.10.1.0-SNAPSHOT.jar
2.    kafka-clients-0.10.1.0-SNAPSHOT.jar

## Setup Instructions

#### Extact the kafka_2.11-0.10.1.0-SNAPSHOT.tgz file ####
    tar -xvzf kafka_2.11-0.10.1.0-SNAPSHOT.tgz

#### Install the Json-Data-Generator  
Download the latest [json-data-generator release](https://github.com/acesinc/json-data-generator/releases) and follow the install instructions [here](http://acesinc.net/introducing-a-streaming-json-data-generator/)

#### Setup the kafka-streams repo
Clone or fork the repo
```
     git clone git@github.com:bbejeck/kafka-streams    
     cd kafka-streams
```     
Then copy the json config files to json generator conf directory
```
    cp streaming-workflows/* <dir>/json-data-generator-1.2.0/conf
```    

Create all the topics required by the examples
```
     ./bin/create-topics.sh /usr/local/kafka_2.11-0.10.1.0 localhost 2181
     args are kafka home, zookeeper host and zookeeper port adjust accordingly
```     

#### Prepare to run the examples
Start zookeeper and kafka
```
      kafka-install-dir/bin/zookeeper-server-start.sh ../conf/zookeeper.properties
      kafka-install-dir/bin/kafka-server-start ../conf/server.properties
```
### Running the Patient Processor API KStreams API for Drools Rule Processor ###
     cd <dir>/json-data-generator-1.2.0/
     java -jar json-data-generator-1.2.0 patients-config.json
     cd kafka-streams
     ./gradlew runPatientStreams



### Running the Purchase Processor API KStreams API Examples ###
     cd <dir>/json-data-generator-1.2.0/
     java -jar json-data-generator-1.2.0 purchases-config.json
     cd kafka-streams
     ./gradlew runPurchaseProcessor | runPurchaseStreams


### Running the Stock Trades Processor API or KStreams API Examples ###
     cd <dir>/json-data-generator-1.2.0/
     java -jar json-data-generator-1.2.0 stock-transactions-config.json
     cd kafka-streams
     ./gradlew runStockProcessor | runStockStreams

### Running the Twitter KStreams Language Classification Example ###
    rename src/main/resources/twitter-app.properties.template to twitter-app.properties
    fill out the properties file with all the required values

    cd kafka-streams
    ./gradlew runTwitterKstreamNLP

### Viewing the results of the purchase streaming examples ###
    cd kafka_install-dir/bin
    ./kafka-console-consumer --topic [patterns|rewards|purchases] --zookeeper localhost:2181

### Viewing the results of the stock-trading streaming examples ###
    cd kafka_install-dir/bin
    ./kafka-console-consumer --topic [stocks-out|transaction-summary] --zookeeper localhost:2181

### Viewing the results of the Twitter KStreams Language Classification Example ###
    cd kafka_install-dir/bin
    ./kafka-console-consumer --topic [english|french|spanish] --zookeeper localhost:2181    
