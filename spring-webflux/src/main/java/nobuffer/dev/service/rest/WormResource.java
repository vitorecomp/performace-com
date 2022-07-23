package nobuffer.dev.service.rest;

import io.swagger.v3.oas.annotations.Parameter;
import nobuffer.dev.service.domain.ResourceUsage;
import nobuffer.dev.service.domain.Sleep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController()
@RequestMapping("/worm")
public class WormResource {
    Logger log = LoggerFactory.getLogger(SleepResources.class);

    @PostMapping(value = "/fullworm", produces = "application/json")
    public Mono<ResourceUsage> postWorm() throws InterruptedException {
        log.info("Start the fullworm");
        ResourceUsage resourceUsage = new ResourceUsage();
        return Mono.fromCallable(() -> {
            return resourceUsage;
        });
    }

    @GetMapping(value = "/hashing/{hashAmount}", produces = "application/json")
    public Mono<ResourceUsage> useCpu(@Parameter(description = "Sleep time", required = true) @PathVariable Long hashAmount) throws InterruptedException {
        log.info("Start the hashing");
        ResourceUsage resourceUsage = new ResourceUsage();
        return Mono.fromCallable(() -> {
            return resourceUsage;
        });
    }

    @GetMapping(value = "/memoryLeak/{leakedMemory}", produces = "application/json")
    public Mono<ResourceUsage> useMemory(@Parameter(description = "Sleep time", required = true) @PathVariable Long leakedMemory) throws InterruptedException {
        log.info("Start the fullworm");
        ResourceUsage resourceUsage = new ResourceUsage();
        return Mono.fromCallable(() -> {
            return resourceUsage;
        });
    }
}
