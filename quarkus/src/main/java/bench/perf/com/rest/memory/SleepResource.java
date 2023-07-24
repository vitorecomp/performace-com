package bench.perf.com.rest.memory;

import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.domain.memory.Sleep;
import bench.perf.com.service.memory.SleepService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;


@Path("/resource-wait-benchmark")
public class SleepResource {


    SleepService sleepService;

    @Inject
    SleepResource(SleepService sleepService){
        this.sleepService = sleepService;
    }

    @GET
    @Path("/async-sleep")
    public Uni<RequestStatistics> getSleep(@QueryParam("sleepTime") @DefaultValue("1000") Long sleepTime){  
        Sleep sleep = new Sleep(sleepTime);
        return sleepService.asyncSleep(sleep);
    }


    @GET
    @Path("/sync-sleep")
    public RequestStatistics getSleepThread(@QueryParam("sleepTime") @DefaultValue("1000") Long sleepTime) {
        Sleep sleep = new Sleep(sleepTime);
        return sleepService.syncSleep(sleep);
    }
}
