package bench.perf.com.service.kafka;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.domain.kafka.KafkaMessage;
import bench.perf.com.domain.kafka.KafkaRequest;
import bench.perf.com.utility.MessageUtils;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KafkaService {

    @Inject
    @Channel("kafka-prog-send")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 100000)
    Emitter<KafkaMessage> benchEmitter;
    
    public RequestStatistics send(KafkaRequest request) {
        RequestStatistics stats = new RequestStatistics();
        stats.setNumMessages(request.getNumMessages());
        stats.setMessageSize(request.getMessageSize());
        stats.startTimer();

        String message = request.getMessage();
        if(message == null){
            message = MessageUtils.generateMessage(request.getMessageSize());
        }
        
        Integer messages = request.getNumMessages() != null ? request.getNumMessages() : 1;
        
        KafkaMessage kafkaMessage = new KafkaMessage(message);
        
        for (int i = 0; i < messages; i++) {
            Log.info("Sending message");  
            benchEmitter.send(kafkaMessage);
        }
        
        stats.calculateDuration();
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
