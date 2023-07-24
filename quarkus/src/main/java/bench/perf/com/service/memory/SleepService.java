package bench.perf.com.service.memory;

import java.time.Duration;

import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.domain.memory.Sleep;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class SleepService {

    public Uni<RequestStatistics> asyncSleep(Sleep sleep) {
        RequestStatistics stats = new RequestStatistics();
        stats.startTimer();
        stats.setSleepDuration(sleep.getSleepTime());
        return Uni
                .createFrom()
                .item(stats)
                .onItem()
                .delayIt()
                .by(Duration.ofMillis(sleep.getSleepTime()))
                .onItem()
                .transform((statsReq) -> {                    
                    statsReq.calculateDuration();
                    return statsReq;
                });

    }

    public RequestStatistics syncSleep(Sleep sleep) {
        // sleep the requested time
        RequestStatistics stats = new RequestStatistics();
        stats.startTimer();
        stats.setSleepDuration(sleep.getSleepTime());
        try {
            Thread.sleep(sleep.getSleepTime());
        } catch (InterruptedException e) {
            Log.warn("Sleep interrupted", e);
        }
        stats.calculateDuration();
        return stats;
    }

}
