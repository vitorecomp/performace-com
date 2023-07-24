package bench.perf.com.domain;

import java.time.Duration;
import java.time.LocalTime;

public class RequestStatistics {
    Integer numMessages;
    Integer messageSize;
    Long durationMillis;
    Long sleepTimeMillis;

    public Long getSleepTimeMillis() {
        return sleepTimeMillis;
    }

    public RequestStatistics() {
    }

    private LocalTime startTime;

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

    public void calculateDuration() {
        LocalTime endTime = LocalTime.now();
        Duration duration = Duration.between(startTime, endTime);
        this.setDuration(duration.toMillis());
    }

    public void startTimer() {
        startTime = LocalTime.now();

    }

    public void setSleepDuration(Long sleepTimeMillis) {
        this.sleepTimeMillis = sleepTimeMillis;
    }

}
