package bench.perf.com.domain;


public class RequestStatistics {
    Integer numMessages;
    Integer messageSize;
    Long durationMillis;

    public Integer getNumMessages() {
        return numMessages;
    }
    public void setNumMessages(Integer numMessages) {
        this.numMessages = numMessages;
    }
    public Integer getMessageSize() {
        return messageSize;
    }
    public void setMessageSize(Integer messageSize) {
        this.messageSize = messageSize;
    }
    public Long getDuration() {
        return durationMillis;
    }
    public void setDuration(Long durationMillis) {
        this.durationMillis = durationMillis;
    }
    public RequestStatistics(Integer numMessages, Integer messageSize, Long durationMillis) {
        this.numMessages = numMessages;
        this.messageSize = messageSize;
        this.durationMillis = durationMillis;
    }
    public RequestStatistics() {
    }

    
    
}
