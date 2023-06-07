package bench.perf.com.service;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.jboss.logging.Logger;

import bench.perf.com.domain.KafkaMessage;
import bench.perf.com.domain.KafkaRequest;
import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.utility.MessageUtils;
import jakarta.inject.Inject;

public class KafkaService {

    private final Logger LOG = Logger.getLogger(KafkaService.class);


    @Inject
    @Channel("kakfa-prog-send")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 50000)
    Emitter<KafkaMessage> benchEmitter;

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
            LOG.info("Sending the "+ n +" block of messages");
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
}