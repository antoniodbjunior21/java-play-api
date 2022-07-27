package repository;

import search.SearchFilter;
import search.SearchResult;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface BaseRepository<T> {
    CompletionStage<T> findById(Long id);
    <K> CompletionStage<K> findById(Long id, Function<? super T, ? extends K> converter);
    <K> CompletionStage<Optional<K>> get(Long id, Function<? super T, ? extends K> converter);
    T findById(EntityManager em, Long id);

    CompletionStage<Optional<T>> merge(T entity);
    <K> CompletionStage<Optional<K>> merge(T entity, Function<? super T, ? extends K> converter);

    CompletionStage<SearchResult<T>> filter(SearchFilter<T> filter);
    <K> CompletionStage<SearchResult<K>> filter(SearchFilter<T> filter, Function<? super T, ? extends K> converter);
    CompletionStage<Void> remove(Long id);
    void remove(EntityManager em, T entity);
}
