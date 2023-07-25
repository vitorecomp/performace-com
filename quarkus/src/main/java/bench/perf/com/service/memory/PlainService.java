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
    public SimpleStructure simpleStructure(String uuid) {
        return new SimpleStructure(uuid);
    }

    public List<SimpleStructure> simpleStructureList(Integer numberOfStructs) {
        List<SimpleStructure> simpleStructures = new ArrayList<>(numberOfStructs);
        for (Integer i = 0; i < numberOfStructs; i++) {
            simpleStructures.set(i, new SimpleStructure());
        }
        return simpleStructures;
    }

    // complex json services
    public ComplexStructure complexStructure(String uuid) {
        return new ComplexStructure(uuid);

    }

    public List<ComplexStructure> complexStructureList(Integer numberOfStructs) {
        List<ComplexStructure> complexStructures = new ArrayList<>(numberOfStructs);
        for (Integer i = 0; i < numberOfStructs; i++) {
            complexStructures.set(i, new ComplexStructure());
        }
        return complexStructures;
    }
}
