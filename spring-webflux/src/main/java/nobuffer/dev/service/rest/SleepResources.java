package nobuffer.dev.service.rest;

import io.swagger.v3.oas.annotations.Parameter;
import nobuffer.dev.service.domain.Sleep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("/sleep")
public class SleepResources {

    Logger log = LoggerFactory.getLogger(SleepResources.class);

    @GetMapping(value = "/{sleepTime}", produces = "application/json")
    public Mono<Sleep> getSleepThread(@Parameter(description = "Sleep time", required = true) @PathVariable Long sleepTime) throws InterruptedException {
        log.info("Start the thread sleep");
        Sleep sleep = new Sleep();
        sleep.setTime(sleepTime);
        sleep.setStartDate(LocalDateTime.now());


        return Mono.fromCallable(() -> {
            log.info("Move to async call");
            log.info("Finish the sleep to async call");
            sleep.setEndDate(LocalDateTime.now());
            return sleep;
        }).delayElement(Duration.ofMillis(sleepTime));
    }
}
