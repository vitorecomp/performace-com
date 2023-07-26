package bench.perf.com.rest.filesystem;

import java.util.List;

import bench.perf.com.service.filesystem.FileSystemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

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

    @GET
    @Path("/small-files/{id}")
    public String getSmallFile() {
        return fileSystemService.smallFileRead();
    }

    @GET
    @Path("/large-files")
    public List<String> getLargeFiles() {
        return fileSystemService.largeFilesRead();
    }

    @GET
    @Path("/large-files/{id}")
    public String getLargeFile() {
        return fileSystemService.largeFileRead();
    }

}
