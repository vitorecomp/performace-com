package bench.perf.com.domain.kafka;

public class KafkaRequest {
    private String topic;
    private String message;

    private Integer numMessages;
    private Integer messageSize;

    public KafkaRequest() {
    }

    public KafkaRequest(String topic, String message, Integer numMessages, Integer messageSize) {
        this.topic = topic;
        this.message = message;
        this.numMessages = numMessages;
        this.messageSize = messageSize;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getNumMessages() {
        return this.numMessages;
    }

    public Integer getMessageSize() {
        return this.messageSize;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumMessages(Integer numMessages) {
        this.numMessages = numMessages;
    }

    public void setMessageSize(Integer messageSize) {
        this.messageSize = messageSize;
    }
}
