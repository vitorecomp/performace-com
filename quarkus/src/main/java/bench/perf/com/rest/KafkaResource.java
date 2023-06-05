package bench.perf.com.rest;

import bench.perf.com.domain.KafkaRequest;
import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.service.KafkaService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/kafka-benchmark")
public class KafkaResource {
    
    KafkaService kafkaService;

    @Inject
    KafkaResource(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @POST
    @Path("/simple-producer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @IfBuildProperty(name = "kafka-producer-enabled", stringValue = "true")
    public void benchmark(KafkaRequest request) throws InterruptedException {

        Uni.createFrom().item(request).emitOn(Infrastructure.getDefaultWorkerPool()).subscribe().with(
                this::runKafkaService, Throwable::printStackTrace
        );

        Log.info("Running benchmark");
    }

    private void runKafkaService( KafkaRequest request){
        try {
            kafkaService.run(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
