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
            @QueryParam("numberOfMessages") @DefaultValue("1000") Integer numberOfMessages) {
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
    public List<SimpleStructure> getSimpleJsonList() {
        return plainService.simpleStructureList();
    }

    @GET
    @Path("/simple-json/{id}")
    public SimpleStructure getSimpleJson() {
        return plainService.simpleStructure();
    }

    @GET
    @Path("/complex-json")
    public List<ComplexStructure> getComplexJsonList() {
        return plainService.complexStructureList();
    }

    @GET
    @Path("/complex-json/{id}")
    public ComplexStructure getComplexJson() {
        return plainService.complexStructure();
    }

    @GET
    @Path("/small-files")
    public List<String> getSmallFiles() {
        return plainService.smallFilesRead();
    }

    @GET
    @Path("/small-files/{id}")
    public String getSmallFile() {
        return plainService.smallFileRead();
    }

    @GET
    @Path("/large-files")
    public List<String> getLargeFiles() {
        return plainService.largeFilesRead();
    }

    @GET
    @Path("/large-files/{id}")
    public String getLargeFile() {
        return plainService.largeFileRead();
    }
}