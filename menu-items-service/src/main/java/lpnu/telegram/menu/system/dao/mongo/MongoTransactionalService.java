package lpnu.telegram.menu.system.dao.mongo;

import com.mongodb.reactivestreams.client.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.dao.TransactionalService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Slf4j
public class MongoTransactionalService implements TransactionalService {
    private final MongoClient mongoClient;

    @Override
    public <T> Mono<Void> performTransitionally(Mono<T> action) {
        return Mono.from(mongoClient.startSession())
                .publishOn(Schedulers.boundedElastic())
                .handle((clientSession, sink) -> {
                    log.info("Starting transaction");
                    clientSession.startTransaction();
                    log.info("Transaction started...");
                    action.subscribe(res -> {
                        sink.complete();
                        log.info("Action executed, committing...");
                        Mono.from(clientSession.commitTransaction())
                                .subscribe();
                    }, err -> {
                        log.error("Error occurred", err);
                        sink.error(err);
                        Mono.from(clientSession.abortTransaction())
                                .subscribe();
                    });
                });
    }
}
