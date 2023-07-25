package bench.perf.com.domain.memory;

import java.util.List;
import java.util.UUID;

import bench.perf.com.utility.MessageUtils;

public class SimpleStructure {
    String uuid;
    String name;
    
    Integer age;
    String address;

    List<String> messages;

    public SimpleStructure() {
        this(SimpleStructure.genUUID());
    }

    public SimpleStructure(String uuid) {
        this.uuid = uuid;

        this.name = MessageUtils.generateMessage(10);
        this.age = (int) (Math.random() * 100);

        this.address = MessageUtils.generateMessage(100);

        for(Integer i = 0; i < Math.random() * 500; i++) {
            this.messages.add(MessageUtils.generateMessage(100));
        }
    }

    static public String genUUID() {
        return UUID.randomUUID().toString();
    }
}
