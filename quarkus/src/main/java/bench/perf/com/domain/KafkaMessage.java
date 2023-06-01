package bench.perf.com.domain;

public class KafkaMessage {
    String message;

    public KafkaMessage(){
    }
    
    public KafkaMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
