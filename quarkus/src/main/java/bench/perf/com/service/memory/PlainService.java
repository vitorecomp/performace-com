package bench.perf.com.service.memory;

import java.util.ArrayList;
import java.util.List;

import bench.perf.com.domain.memory.ComplexStructure;
import bench.perf.com.domain.memory.SimpleStructure;
import bench.perf.com.utility.MessageUtils;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlainService {

    // plain text string services
    public String plainString(Integer id, Integer messageSize) {
        return id.toString() + "-" + MessageUtils.generateMessage(messageSize);
    }

    public List<String> plainStringList(Integer messageSize, Integer numberOfMessages) {
        List<String> strings = new ArrayList<>(numberOfMessages);
        for (Integer i = 0; i < numberOfMessages; i++) {
            strings.set(i, MessageUtils.generateMessage(messageSize));
        }
        return strings;
    }

    // plain text json services
    public SimpleStructure simpleStructure() {
        return null;
    }

    public List<SimpleStructure> simpleStructureList() {
        return null;
    }

    // complex json services
    public ComplexStructure complexStructure() {
        return null;
    }

    public List<ComplexStructure> complexStructureList() {
        return null;
    }

    // small file services
    public List<String> smallFilesRead() {
        return null;
    }

    public String smallFileRead() {
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
