package bench.perf.com.rest.filesystem;

import java.util.List;

import bench.perf.com.service.filesystem.FileSystemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@ApplicationScoped
@Path("/filesystem")
public class FileSystemResource {

    FileSystemService fileSystemService;

    @Inject
    FileSystemResource(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @GET
    @Path("/small-files")
    public List<String> getSmallFiles(@QueryParam("limit") @DefaultValue("100") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset) {
        return fileSystemService.readSmallFiles(limit, offset);
    }

    @POST
    @Path("/small-files")
    public String postSmallFiles(String fileContent) {
        return fileSystemService.saveSmallFile(fileContent);
    }

    @PUT
    @Path("/small-files/{id}")
    public String putSmallFiles(@PathParam("id") String id, String fileContent) {
        String responseFile = fileSystemService.updateSmallFile(id, fileContent);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

    @GET
    @Path("/small-files/{id}")
    public String getSmallFile(@PathParam("id") String id) {
        String responseFile = fileSystemService.readSmallFile(id);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

    @DELETE
    @Path("/small-files/{id}")
    public String deleteSmallFile(@PathParam("id") String id) {
        return fileSystemService.deleteSmallFile(id);
    }

    @GET
    @Path("/large-files")
    public List<String> getLargeFiles(@QueryParam("limit") @DefaultValue("100") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset) {
        return fileSystemService.readLargeFiles(limit, offset);
    }

    @POST
    @Path("/large-files")
    public String postLargeFiles(String fileContent) {
        return fileSystemService.saveLargeFile(fileContent);
    }

    @PUT
    @Path("/large-files")
    public String putLargeFiles(@PathParam("id") String id, String fileContent) {
        String responseFile = fileSystemService.updateLargeFile(id, fileContent);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

    @GET
    @Path("/large-files/{id}")
    public String getLargeFile(@PathParam("id") String id) {
        String responseFile = fileSystemService.readLargeFile(id);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

    @DELETE
    @Path("/large-files/{id}")
    public String deleteLargeFile(@PathParam("id") String id) {
        String responseFile = fileSystemService.deleteLargeFileRead(id);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

}
