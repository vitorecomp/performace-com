package nobuffer.dev.service.rest;

import io.swagger.v3.oas.annotations.Parameter;
import nobuffer.dev.service.domain.Sleep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("/sleep")
public class SleepResources {

    Logger log = LoggerFactory.getLogger(SleepResources.class);

    @Async
    @GetMapping(value = "/non-block/{sleepTime}", produces = "application/json")
    public CompletableFuture<Sleep> getSleep(@Parameter(description = "Sleep time", required = true) @PathVariable Long sleepTime) throws InterruptedException {
        log.info("Start the thread sleep");
        Sleep sleep = new Sleep();
        sleep.setTime(sleepTime);
        sleep.setStartDate(LocalDateTime.now());
        log.info("Move to async call");


        Executor delayed = CompletableFuture.delayedExecutor(sleepTime, TimeUnit.MILLISECONDS);
        return CompletableFuture.supplyAsync(() -> {
            log.info("Finish the sleep to async call");
            sleep.setEndDate(LocalDateTime.now());
            return sleep;
        }, delayed);
    }

    @GetMapping(value = "/block/{sleepTime}", produces = "application/json")
    public Sleep getSleepThread(@Parameter(description = "Sleep time", required = true) @PathVariable Long sleepTime) throws InterruptedException {
        log.info("Start the thread sleep");
        Sleep sleep = new Sleep();
        sleep.setTime(sleepTime);
        sleep.setStartDate(LocalDateTime.now());
        log.info("Move to async call");
        Thread.sleep(sleepTime);
        log.info("Finish the sleep to async call");
        sleep.setEndDate(LocalDateTime.now());
        return sleep;
    }
}
