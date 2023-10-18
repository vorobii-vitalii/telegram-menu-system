package lpnu.telegram.menu.system.dao;

import reactor.core.publisher.Mono;

public interface TransactionalService {
    <T> Mono<Void> performTransitionally(Mono<T> action);
}
