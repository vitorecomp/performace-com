package bench.perf.com.service.filesystem;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileSystemService {
        // small file services
    public List<String> smallFilesRead() {
        //look if the small dir exists
        //look if the number of files is small that the maximum
        //in case not create a new files and read them
        return null;
    }

    public String smallFileRead() {
        //look if the id is in the max, if not return error
        //create of read the id file
        return null;
    }

    // large file services
    public List<String> largeFilesRead() {
        return null;
    }

    public String largeFileRead() {
        return null;
    }
}
