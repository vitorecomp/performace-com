package bench.perf.com.rest.filesystem;

import java.io.IOException;
import java.util.List;

import bench.perf.com.domain.filesystem.FileProprieties;
import bench.perf.com.service.filesystem.FileSystemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;


//TODO create a try catch in all methods
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
            @QueryParam("offset") @DefaultValue("0") Integer offset) throws IOException {
        return fileSystemService.readSmallFiles(limit, offset);
    }

    @POST
    @Path("/small-files")
    public String postSmallFiles(FileProprieties fileProprieties) {
        try {
            String responseFile = fileSystemService.saveSmallFile(fileProprieties);
            if (responseFile == null) {
                throw new BadRequestException("The file size is to big, use the large files endpoint");
            }
            return responseFile;
        } catch (IOException e) {
            throw new InternalServerErrorException("Error while saving the file");
        }
    }

    @PUT
    @Path("/small-files/{id}")
    public String putSmallFiles(@PathParam("id") String id, String fileContent) {
        try {
            String responseFile = fileSystemService.updateSmallFile(id, fileContent);
            if (responseFile == null) {
                throw new NotFoundException();
            }
            return responseFile;
        } catch (IOException e) {
            throw new InternalServerErrorException("Error while saving the file");
        }
    }

    @GET
    @Path("/small-files/{id}")
    public String getSmallFile(@PathParam("id") String id) {
        try {
            String responseFile = fileSystemService.readSmallFile(id);
            if (responseFile == null) {
                throw new NotFoundException();
            }
            return responseFile;
        } catch (IOException e) {
            throw new InternalServerErrorException("Error while reading the file");
        }
    }

    @DELETE
    @Path("/small-files/{id}")
    public String deleteSmallFile(@PathParam("id") String id) throws IOException {
        return fileSystemService.deleteSmallFile(id);
    }

    @GET
    @Path("/large-files")
    public List<String> getLargeFiles(@QueryParam("limit") @DefaultValue("100") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset) throws IOException {
        return fileSystemService.readLargeFiles(limit, offset);
    }

    @POST
    @Path("/large-files")
    public String postLargeFiles(FileProprieties fileProprieties) throws IOException {
        String responseFile = fileSystemService.saveLargeFile(fileProprieties);
        if (responseFile == null) {
            throw new BadRequestException("The file size is to small, use the small files endpoint");
        }
        return responseFile;
    }

    @PUT
    @Path("/large-files")
    public String putLargeFiles(@PathParam("id") String id, String fileContent) throws IOException {
        String responseFile = fileSystemService.updateLargeFile(id, fileContent);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

    @GET
    @Path("/large-files/{id}")
    public String getLargeFile(@PathParam("id") String id) throws IOException {
        String responseFile = fileSystemService.readLargeFile(id);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

    @DELETE
    @Path("/large-files/{id}")
    public String deleteLargeFile(@PathParam("id") String id) throws IOException {
        String responseFile = fileSystemService.deleteLargeFileRead(id);
        if (responseFile == null) {
            throw new NotFoundException();
        }
        return responseFile;
    }

}
