package bench.perf.com.rest.memory;

import java.util.Collections;
import java.util.List;

import bench.perf.com.domain.memory.ComplexStructure;
import bench.perf.com.domain.memory.SimpleStructure;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/plain-benchmarks")
public class PlainResource {

    @GET
    @Path("/plain-texts")
    public String getPlain() {
        return "Hello from my benchmark application!";
    }

    @GET
    @Path("/simple-json")
    public List<SimpleStructure> getSimpleJsonList() {
        return Collections.emptyList();

    }

    @GET
    @Path("/simple-json/{id}")
    public SimpleStructure getSimpleJson() {
        return new SimpleStructure();
    }

    @GET
    @Path("/complex-json")
    public List<ComplexStructure> getComplexJsonList() {
        return Collections.emptyList();
    }

    @GET
    @Path("/complex-json/{id}")
    public ComplexStructure getComplexJson() {
        return new ComplexStructure();
    }

    @GET
    @Path("/small-files")
    public String getSmallFiles() {
        return "";
    }

    @GET
    @Path("/small-files/{id}")
    public String getSmallFile() {
        return "";
    }

    @GET
    @Path("/large-files")
    public String getLargeFiles() {
        return "";
    }

    @GET
    @Path("/large-files/{id}")
    public String getLargeFile() {
        return "";
    }
}