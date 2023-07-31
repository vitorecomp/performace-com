package bench.perf.com.rest.filesystem;

import java.util.List;

import bench.perf.com.service.filesystem.FileSystemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

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
    public List<String> getSmallFiles() {
        return fileSystemService.smallFilesRead();
    }

    @POST
    @Path("/small-files")
    public List<String> postSmallFiles() {
        return fileSystemService.smallFilesRead();
    }

    @PUT
    @Path("/small-files")
    public List<String> putSmallFiles() {
        return fileSystemService.smallFilesRead();
    }

    @GET
    @Path("/small-files/{id}")
    public String getSmallFile() {
        return fileSystemService.smallFileRead();
    }

    @DELETE
    @Path("/small-files/{id}")
    public String deleteSmallFile() {
        return fileSystemService.smallFileRead();
    }

    @GET
    @Path("/large-files")
    public List<String> getLargeFiles() {
        return fileSystemService.largeFilesRead();
    }

    @POST
    @Path("/large-files")
    public List<String> postLargeFiles() {
        return fileSystemService.smallFilesRead();
    }

    @PUT
    @Path("/large-files")
    public List<String> putLargeFiles() {
        return fileSystemService.smallFilesRead();
    }

    @GET
    @Path("/large-files/{id}")
    public String getLargeFile() {
        return fileSystemService.largeFileRead();
    }

    @DELETE
    @Path("/large-files/{id}")
    public String deleteLargeFile() {
        return fileSystemService.largeFileRead();
    }

}
