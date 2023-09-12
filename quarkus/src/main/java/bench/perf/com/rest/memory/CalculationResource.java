package bench.perf.com.rest.memory;

import bench.perf.com.domain.RequestStatistics;
import bench.perf.com.service.memory.CalculationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;


//TODO add the capability to use bandwith
@ApplicationScoped
@Path("/cpu-calculation")
public class CalculationResource {


    CalculationService calculationService;

    @Inject
    CalculationResource() {
    }
    

    @Path("/hash")
    @GET
    public RequestStatistics hash(@QueryParam("value") String value) {
        return CalculationService.hash(value);
    }


    @Path("/md5")
    @GET
    public RequestStatistics md5(@QueryParam("value") Integer value) {
        return CalculationService.md5(value);
    }


    @Path("/fibonacci")
    @GET
    public RequestStatistics fibonacci(@QueryParam("value") Integer value) {
        return CalculationService.fibonacci(value);
    }

    @Path("/recursive-factorial")
    @GET
    public RequestStatistics factorial(@QueryParam("value") Integer value) {
        return CalculationService.factorial(value);
    }


    @Path("/prime")
    @GET
    public RequestStatistics prime(@QueryParam("value") Integer value) {
        return CalculationService.prime(value);
    }

    @Path("/sort")
    @GET
    public RequestStatistics sort(@QueryParam("value") Integer value) {
        return CalculationService.sort(value);
    }

    
    @Path("/sort")
    @POST
    public RequestStatistics sortVector(@QueryParam("value") Integer value) {
        return CalculationService.sortVector(value);
    }
}
