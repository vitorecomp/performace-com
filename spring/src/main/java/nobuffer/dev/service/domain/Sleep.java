package nobuffer.dev.service.domain;

import java.time.LocalDateTime;

public class Sleep {
    Long time;
    LocalDateTime StartDate;
    LocalDateTime EndDate;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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
