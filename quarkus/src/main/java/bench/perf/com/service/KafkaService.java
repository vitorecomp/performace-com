package bench.perf.com.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import bench.perf.com.domain.KafkaMessage;
import bench.perf.com.domain.KafkaRequest;
import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.utility.MessageUtils;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KafkaService {

    // @Outgoing("kafka-prog-send")
    // public Multi<KafkaMessage> generate() {
    //     // Build an infinite stream of random prices
    //     // It emits a price every second
    //     return Multi.createFrom().ticks().every(Duration.ofNanos(1000000))
    //         .map(x -> {
    //             return new KafkaMessage(MessageUtils.generateMessage(2048));
    //         });
    // }


    @Inject
    @Channel("kafka-prog-send")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 100000)
    Emitter<KafkaMessage> benchEmitter;
    
    public RequestStatistics run(KafkaRequest request) {
        RequestStatistics stats = new RequestStatistics();
        stats.setNumMessages(request.getNumMessages());
        stats.setMessageSize(request.getMessageSize());
        LocalTime startTime = LocalTime.now();
        
        String message = request.getMessage();
        if(message == null){
            message = MessageUtils.generateMessage(request.getMessageSize());
        }
        
        Integer messages = request.getNumMessages() != null ? request.getNumMessages() : 1;
        
        KafkaMessage kafkaMessage = new KafkaMessage(message);
        
        for (int i = 0; i < messages; i++) {
            Log.info("Seding message");  
            benchEmitter.send(kafkaMessage);
        }
        
        LocalTime endTime = LocalTime.now();

        Duration duration = Duration.between(startTime, endTime);
        stats.setDuration(duration.toMillis());
        return stats;
    }

    @Incoming("kafka-prog-recv")
    public CompletionStage<Void> consume(Message<KafkaMessage> record) {
        IncomingKafkaRecordMetadata<?, ?> md = KafkaMetadataUtil.readIncomingKafkaMetadata(record).orElseThrow(IllegalStateException::new);
        String msg =
                "Received from Kafka key: %d; partition: %d, topic: %s";
        msg = String.format(msg, md.getKey(), md.getPartition(), md.getTopic());
        Log.info(msg);
        return record.ack();
    }
}
