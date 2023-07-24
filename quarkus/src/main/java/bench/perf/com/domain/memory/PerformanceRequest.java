package bench.perf.com.domain.memory;

import bench.perf.com.domain.Path;

public class PerformanceRequest {

    public enum CpuUsageType {
        HASH, LOOP, RUN_TIME
    };

    private CpuUsageType cpuUsageType;
    private Integer cpuTime;
    private Integer numHashes;
    private Integer loopCount;

    public enum MemoryUsageType {
        DYNAMIC_ALLOCATION, STATIC_ALLOCATION
    };

    private MemoryUsageType memoryUsageType;
    private Integer memoryUsageKb;

    private Path path;

    // generate the constructor using fields
    public PerformanceRequest(CpuUsageType cpuUsageType, Integer cpuTime, Integer numHashes, Integer loopCount,
            MemoryUsageType memoryUsageType, Integer memoryUsageKb, Path path) {
        this.cpuUsageType = cpuUsageType;
        this.cpuTime = cpuTime;
        this.numHashes = numHashes;
        this.loopCount = loopCount;
        this.memoryUsageType = memoryUsageType;
        this.memoryUsageKb = memoryUsageKb;
    }

    public CpuUsageType getCpuUsageType() {
        return cpuUsageType;
    }

    public void setCpuUsageType(CpuUsageType cpuUsageType) {
        this.cpuUsageType = cpuUsageType;
    }

    public Integer getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(Integer cpuTime) {
        this.cpuTime = cpuTime;
    }

    public Integer getNumHashes() {
        return numHashes;
    }

    public void setNumHashes(Integer numHashes) {
        this.numHashes = numHashes;
    }

    public Integer getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(Integer loopCount) {
        this.loopCount = loopCount;
    }

    public MemoryUsageType getMemoryUsageType() {
        return memoryUsageType;
    }

    public void setMemoryUsageType(MemoryUsageType memoryUsageType) {
        this.memoryUsageType = memoryUsageType;
    }

    public Integer getMemoryUsageKb() {
        return memoryUsageKb;
    }

    public void setMemoryUsageKb(Integer memoryUsageKb) {
        this.memoryUsageKb = memoryUsageKb;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

}
