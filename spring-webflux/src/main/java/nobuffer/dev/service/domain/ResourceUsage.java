package nobuffer.dev.service.domain;

import java.time.LocalDateTime;

public class ResourceUsage {
    Long hashs;
    Long memoryUsed;
    LocalDateTime StartDate;
    LocalDateTime EndDate;

    public Long getHashs() {
        return hashs;
    }

    public void setHashs(Long hashs) {
        this.hashs = hashs;
    }

    public Long getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public LocalDateTime getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        StartDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        EndDate = endDate;
    }
}
