package bench.perf.com.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import bench.perf.com.domain.KafkaMessage;
import bench.perf.com.domain.KafkaRequest;
import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.utility.MessageUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KafkaService {

    private static final Logger LOG = Logger.getLogger(KafkaService.class);

    @Inject
    @Channel("kafka-prog-send")
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
            LOG.info("Seding message");  

            benchEmitter.send(kafkaMessage);
            //TODO add here with ack or not
        }
        
        LocalTime endTime = LocalTime.now();

        Duration duration = Duration.between(startTime, endTime);
        stats.setDuration(duration.toMillis());
        return stats;
    }

    @Incoming("kafka-prog-recv")
    public CompletionStage<Void> consume(Message<KafkaMessage> record) {
        LOG.info("Message Received");  
        //todo get the time
        return record.ack();
    }
}
