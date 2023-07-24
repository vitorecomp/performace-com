package bench.perf.com.domain.memory;

public class Sleep {
    Long sleepTime;

    public Sleep(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }
}
