package bench.perf.com.service.filesystem;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileSystemService {
    public String saveSmallFile(String fileContent) {
        return null;
    }

    public String updateSmallFile(String id, String fileContent) {
        return null;
    }

    public List<String> readSmallFiles(Integer limit, Integer offset) {
                //look if the small dir exists
        //look if the number of files is small that the maximum
        //in case not create a new files and read them
        return null;
    }

    public String readSmallFile(String id) {
                //look if the id is in the max, if not return error
        //create of read the id file
        return null;
    }

    public String deleteSmallFile(String id) {
        return null;
    }

    //large file services
    public List<String> readLargeFiles(Integer limit, Integer offset) {
        return null;
    }

    public String saveLargeFile(String fileContent) {
        return null;
    }

    public String deleteLargeFileRead(String id) {
        return null;
    }

    public String readLargeFile(String id) {
        return null;
    }

    public String updateLargeFile(String id, String fileContent) {
        return null;
    }

}
