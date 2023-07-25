package bench.perf.com.rest.memory;

import java.util.List;

import bench.perf.com.domain.memory.ComplexStructure;
import bench.perf.com.domain.memory.SimpleStructure;
import bench.perf.com.service.memory.PlainService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@Path("/plain-benchmarks")
public class PlainResource {

    PlainService plainService;

    @Inject
    public PlainResource(PlainService plainService) {
        this.plainService = plainService;
    }

    @GET
    @Path("/plain-texts")
    public List<String> getPlainList(@QueryParam("messageSize") @DefaultValue("1000") Integer messageSize,
            @QueryParam("numberOfMessages") @DefaultValue("10") Integer numberOfMessages) {
        return plainService.plainStringList(messageSize, numberOfMessages);
    }

    @GET
    @Path("/plain-texts/{id}")
    public String getPlain(@QueryParam("messageSize") @DefaultValue("1000") Integer messageSize,
            @PathParam("id") Integer id) {
        return plainService.plainString(id, messageSize);
    }

    @GET
    @Path("/simple-json")
    public List<SimpleStructure> getSimpleJsonList(
            @QueryParam("numberOfMessages") @DefaultValue("10") Integer numberOfMessages) {
        return plainService.simpleStructureList(numberOfMessages);
    }

    @GET
    @Path("/simple-json/{uuid}")
    public SimpleStructure getSimpleJson(@PathParam("uuid") String uuid) {
        return plainService.simpleStructure(uuid);
    }

    @GET
    @Path("/complex-json")
    public List<ComplexStructure> getComplexJsonList(
            @QueryParam("numberOfMessages") @DefaultValue("10") Integer numberOfMessages) {
        return plainService.complexStructureList(numberOfMessages);
    }

    @GET
    @Path("/complex-json/{uuid}")
    public ComplexStructure getComplexJson(@PathParam("uuid") String uuid) {
        return plainService.complexStructure(uuid);
    }

}