package bench.perf.com.service;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import bench.perf.com.domain.KafkaMessage;
import bench.perf.com.domain.KafkaRequest;
import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.utility.MessageUtils;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.inject.Inject;

public class KafkaService {

    @Inject
    @Channel("kakfa-prog-send")
    @IfBuildProperty(name = "kafka-producer-enabled", stringValue = "true")
    Emitter<KafkaMessage> benchEmitter;
    
    @IfBuildProperty(name = "kafka-producer-enabled", stringValue = "true")
    public RequestStatistics run(KafkaRequest request) {
        
        String message = request.getMessage();
        if(message == null){
            message = MessageUtils.generateMessage(request.getMessageSize());
        }

        Integer messages = request.getNumMessages() != null ? request.getNumMessages() : 1;

        KafkaMessage kafkaMessage = new KafkaMessage(message);

        for (int i = 0; i < messages; i++) {
            benchEmitter.send(kafkaMessage);
            //TODO add here with ack or not
        }


        return new RequestStatistics();
    }

    @Incoming("kakfa-prog-receive")
    public CompletionStage<Void> consume(Message<KafkaMessage> record) {
        //todo get the time
        return record.ack();
    }
}
