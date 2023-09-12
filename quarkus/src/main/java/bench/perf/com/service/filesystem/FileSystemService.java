package bench.perf.com.service.filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import bench.perf.com.domain.filesystem.FileProprieties;
import bench.perf.com.utility.MessageUtils;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileSystemService {

    private static final int SMALL_FILE_MAX_SIZE = 1000;
    private static final String SMALL_SIZE_FOLDER_NAME = "small";
    private static final String LARGE_SIZE_FOLDER_NAME = "large";

    private String handleFileWrite(String fileName, String fileContent, String folder, Boolean create)
            throws IOException {
        // look if the small dir exists
        Path path = Paths.get("./" + SMALL_SIZE_FOLDER_NAME);
        if (!Files.exists(path)) {
            // create the dir
            Files.createDirectory(path);
        }

        // FIXME add custom exception to 404
        path = Paths.get("./" + SMALL_SIZE_FOLDER_NAME + "/" + fileName);
        if (!create && !Files.exists(path)) {
            return null;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.append(fileContent);
        writer.close();

        return fileContent;
    }

    public String saveSmallFile(FileProprieties fileProprieties) throws IOException {
        // FIXME add custom exception to 400
        if (fileProprieties.getSize() > SMALL_FILE_MAX_SIZE)
            return null;
        return handleFileWrite(fileProprieties.getName(), MessageUtils.generateMessage(fileProprieties.getSize()),
                SMALL_SIZE_FOLDER_NAME, true);
    }

    public String updateSmallFile(String name, String fileContent) throws IOException {
        // FIXME add custom exception to 400
        if (fileContent.length() > SMALL_FILE_MAX_SIZE)
            return null;
        return handleFileWrite(name, fileContent, SMALL_SIZE_FOLDER_NAME, true);
    }

    private String handleFileRead(String fileName, String folder) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("./" + folder + "/" + fileName);
        if (inputStream == null) {
            return null;
        }
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public List<String> readSmallFiles(Integer limit, Integer offset) throws IOException {
        File f = new File("./" + SMALL_SIZE_FOLDER_NAME);
        String[] pathNames = f.list();
        List<String> filesContent = new ArrayList<>(null);

        //TODO add the limit/offset
        for (String pathname : pathNames) {
            filesContent.add(handleFileRead(pathname, LARGE_SIZE_FOLDER_NAME));
        }

        return filesContent;
    }

    public String readSmallFile(String name) throws IOException {
        return handleFileRead(name, SMALL_SIZE_FOLDER_NAME);
    }


    public String handleFileDelete(String name, String folderName) throws IOException {
        String fileContent = handleFileRead(name, folderName);
        File myObj = new File("./" + SMALL_SIZE_FOLDER_NAME + "/" + name); 
        if (myObj.delete()) { 
          return fileContent;
        } else {
          return null;
        } 

    }
    public String deleteSmallFile(String name) throws IOException {
        return handleFileDelete(name, SMALL_SIZE_FOLDER_NAME);
    }

    // large file services
    public List<String> readLargeFiles(Integer limit, Integer offset) throws IOException {
        File f = new File("./" + LARGE_SIZE_FOLDER_NAME);
        String[] pathNames = f.list();
        List<String> filesContent = new ArrayList<>(null);

        //TODO add the limit/offset
        for (String pathname : pathNames) {
            filesContent.add(handleFileRead(pathname, LARGE_SIZE_FOLDER_NAME));
        }

        return filesContent;
    }

    public String saveLargeFile(FileProprieties fileProprieties) throws IOException {
        // FIXME add custom exception to 400
        if (fileProprieties.getSize() < SMALL_FILE_MAX_SIZE)
            return null;
        return handleFileWrite(fileProprieties.getName(), MessageUtils.generateMessage(fileProprieties.getSize()),
                LARGE_SIZE_FOLDER_NAME, true);
    }

    public String deleteLargeFileRead(String name) throws IOException {
        return handleFileDelete(name, LARGE_SIZE_FOLDER_NAME);
    }

    public String readLargeFile(String name) throws IOException {
        return handleFileRead(name, LARGE_SIZE_FOLDER_NAME);
    }

    public String updateLargeFile(String name, String fileContent) throws IOException {
        // FIXME add custom exception to 400
        if (fileContent.length() < SMALL_FILE_MAX_SIZE)
            return null;
        return handleFileWrite(name, fileContent, LARGE_SIZE_FOLDER_NAME, true);
    }

}
