/*
 * Copyright 2016 Bill Bejeck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cognitive.streams;

import bbejeck.serializer.JsonDeserializer;
import bbejeck.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

import com.cognitive.model.Patient;

import java.util.Properties;

/**
 * User: Tadesse Sefer
 * Date: 3/7/16
 * Time: 5:34 PM
 */
public class KafkaPatientStreamsDriver {

    public static void main(String[] args) {


        StreamsConfig streamsConfig = new StreamsConfig(getProperties());

        JsonDeserializer<Patient> patientJsonDeserializer = new JsonDeserializer<>(Patient.class);
        JsonSerializer<Patient> patientJsonSerializer = new JsonSerializer<>();

        Serde<Patient> patientSerde = Serdes.serdeFrom(patientJsonSerializer,patientJsonDeserializer);

        StringDeserializer stringDeserializer = new StringDeserializer();
        StringSerializer stringSerializer = new StringSerializer();

        Serde<String> stringSerde = Serdes.serdeFrom(stringSerializer,stringDeserializer);

        KStreamBuilder kStreamBuilder = new KStreamBuilder();


        KStream<String, Patient> patientKStream = kStreamBuilder.stream(stringSerde,patientSerde,"src-topic");
        
        patientKStream.process(new ProcessorSupplier<String, Patient>() {
            public Processor<String, Patient> get() {
              return new RuleProcessor();
            }
          });
        
        patientKStream.filter((String key, Patient p) -> p.getEvalSevereSepsisFlag() == 1).to(stringSerde, patientSerde, "drools-processed");

        System.out.println("Starting Patients Drools Streams Processing");
        KafkaStreams kafkaStreams = new KafkaStreams(kStreamBuilder,streamsConfig);
        kafkaStreams.start();
        System.out.println("Now started Patient Drools Streams Processing");

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "Example-Kafka-Streams-Job");
        props.put("group.id", "streams-patients");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testing-streams-api");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return props;
    }

}
