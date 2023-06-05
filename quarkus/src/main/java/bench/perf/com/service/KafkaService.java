package bench.perf.com.service;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.*;

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
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 50000)
    Emitter<KafkaMessage> benchEmitter;

    @IfBuildProperty(name = "kafka-producer-enabled", stringValue = "true")
    public RequestStatistics run(KafkaRequest request) throws InterruptedException {
        
        String message = request.getMessage();
        if(message == null){
            message = MessageUtils.generateMessage(request.getMessageSize());
        }

        Integer messages = request.getNumMessages() != null ? request.getNumMessages() : 1;

        KafkaMessage kafkaMessage = new KafkaMessage(message);

        Integer timesToExecute = request.getTimesToExecute() != null ? request.getTimesToExecute() : 1;

        Integer interval = request.getInterval() != null ? request.getInterval() : 1000;

        for (int n = 0; n < timesToExecute; n++) {
            for (int i = 0; i < messages; i++) {
                benchEmitter.send(kafkaMessage);
                //TODO add here with ack or not
            }
            if(timesToExecute.intValue() > 1){
                Thread.sleep(interval.longValue());
            }

        }




        return new RequestStatistics();
    }

    @Incoming("kakfa-prog-receive")
    public CompletionStage<Void> consume(Message<KafkaMessage> record) {
        //todo get the time
        return record.ack();
    }
}
