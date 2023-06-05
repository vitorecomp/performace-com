package bench.perf.com.scheduled;

import bench.perf.com.domain.KafkaRequest;
import bench.perf.com.service.KafkaService;
import io.quarkus.logging.Log;

//import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@ApplicationScoped
public class ScheduledTrigger {

    @Inject
    ScheduledTrigger(KafkaService kafkaService){
        this.kafkaService = kafkaService;
    }

    int executionTimes = 1;


    KafkaService kafkaService;

    //@Scheduled(cron="{cron.exp}") //10:15 todo dia
    //@Scheduled(every="10s", delayed = "60s", concurrentExecution = Scheduled.ConcurrentExecution.PROCEED)
    public void runBenchmark() throws InterruptedException {
        for (int i = 0; i < executionTimes; i++) {
            Log.debug("starting schedule");
            KafkaRequest request = new KafkaRequest();
            request.setMessageSize(2048);
            request.setNumMessages(10000);
            kafkaService.run(request);
            Log.debug("schedule ok");
        }


    }
}
