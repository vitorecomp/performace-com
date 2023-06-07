package bench.perf.com.commands;

import bench.perf.com.domain.KafkaRequest;
import bench.perf.com.service.KafkaService;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "greeting", mixinStandardHelpOptions = true)
public class KafkaCommand implements Runnable {

    @Parameters(paramLabel = "<numMessages>", defaultValue = "10", description = "The number of messages to send by run.")
    private Integer numMessages;
    @Parameters(paramLabel = "<messageSize>", defaultValue = "2000", description = "The size of each message.")
    private Integer messageSize;
    @Parameters(paramLabel = "<timesToExecute>", defaultValue = "200", description = "The number of executions.")
    private Integer timesToExecute;
    @Parameters(paramLabel = "<interval>", defaultValue = "10000", description = "The interval between the executions.")
    private Integer interval;

    KafkaService kafkaService;

    @Inject
    KafkaCommand(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @Override
    public void run() {
        try {
            KafkaRequest request = new KafkaRequest("kakfa-prog-send", null, numMessages, messageSize, timesToExecute, interval);
            kafkaService.run(request);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
