package bench.perf.com.rest;

import bench.perf.com.domain.Sleep;

public class SleepResource {
    public Sleep getSleep(){   
        return new Sleep();
    }

    public Sleep getSleepThread() {
        return new Sleep();
    }
}
