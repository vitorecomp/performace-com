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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("/sleep")
public class SleepResources {

    Logger log = LoggerFactory.getLogger(SleepResources.class);

    @GetMapping(value = "/non-block/{sleepTime}", produces = "application/json")
    public Mono<Sleep> getSleep(@Parameter(description = "Sleep time", required = true) @PathVariable Long sleepTime) throws InterruptedException {
        log.info("Start the thread sleep non blocking");
        Sleep sleep = new Sleep();
        sleep.setTime(sleepTime);
        sleep.setStartDate(LocalDateTime.now());


        return Mono.fromCallable(() -> {
            log.debug("Move to async call");
            log.debug("Finish the sleep to async call");
            sleep.setEndDate(LocalDateTime.now());
            return sleep;
        }).delayElement(Duration.ofMillis(sleepTime));
    }

    @GetMapping(value = "/block/{sleepTime}", produces = "application/json")
    public Mono<Sleep> getSleepThread(@Parameter(description = "Sleep time", required = true) @PathVariable Long sleepTime) throws InterruptedException {
        log.info("Start the thread sleep blocking");
        Sleep sleep = new Sleep();
        sleep.setTime(sleepTime);
        sleep.setStartDate(LocalDateTime.now());


        return Mono.fromCallable(() -> {
            log.debug("Move to async call");
            log.debug("Finish the sleep to async call");
            Thread.sleep(sleepTime);
            sleep.setEndDate(LocalDateTime.now());
            return sleep;
        });
    }

    @GetMapping(value = "/async/{sleepTime}", produces = "application/json")
    public CompletableFuture<Sleep> getAsyncSleep(@Parameter(description = "Sleep time", required = true) @PathVariable Long sleepTime) throws InterruptedException {
        log.info("Start the thread sleep async");
        Sleep sleep = new Sleep();
        sleep.setTime(sleepTime);
        sleep.setStartDate(LocalDateTime.now());
        log.debug("Move to async call");


        Executor delayed = CompletableFuture.delayedExecutor(sleepTime, TimeUnit.MILLISECONDS);
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Finish the sleep to async call");
            sleep.setEndDate(LocalDateTime.now());
            return sleep;
        }, delayed);
    }
}
