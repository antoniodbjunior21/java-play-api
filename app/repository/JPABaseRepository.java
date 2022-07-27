package repository;

import play.db.jpa.JPAApi;
import search.SearchFilter;
import search.SearchResult;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPABaseRepository<T> {

    public JPAApi jpaApi;
    public DatabaseExecutionContext executionContext;
    public final Class<T> clazz;

    public JPABaseRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext, Class<T> typeParameterClass) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
        this.clazz = typeParameterClass;
    }

    public JPABaseRepository(Class<T> typeParameterClass) {
        this.clazz = typeParameterClass;
    }

    public <t> t wrap(Function<EntityManager, t> function) {
        return jpaApi.withTransaction(function);
    }

    public CompletionStage<Void> remove(Long id) {
        return supplyAsync(() -> jpaApi.withTransaction((em) -> {
            try {
                Constructor<T> ctor = clazz.getConstructor(Long.class);
                T mergedEntity = ctor.newInstance(id);
                remove(em, mergedEntity);
                return null;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }), executionContext);
    }
    public void remove(EntityManager em, T entity){
        entity = em.merge(entity);
        em.remove(entity);
    }

    public CompletionStage<T> findById(Long id) {
        return supplyAsync(() -> wrap(em -> (T) findById(em, id)), executionContext);
    }

    public <K> CompletionStage<K> findById(Long id, Function<? super T, ? extends K> beanConverter) {
        return supplyAsync(() -> wrap(entityManager -> {
            T entity = findById(entityManager, id);
            return beanConverter.apply(entity);
        }), executionContext);
    }

    public CompletionStage<SearchResult<T>> filter(SearchFilter<T> filter) {
        return supplyAsync(() -> wrap(em -> filter.createQuery(em, clazz)), executionContext);
    }

    public <K> CompletionStage<SearchResult<K>> filter(SearchFilter<T> filter, Function<? super T, ? extends K> beanConverter) {
        return supplyAsync(() -> wrap(em -> {
            return filter.createQuery(em, clazz, beanConverter);
        }), executionContext);
    }

    public CompletionStage<Optional<T>> merge(T entity) {
        return supplyAsync(() -> wrap(em -> merge(em, entity)), executionContext);
    }

    public <K> CompletionStage<Optional<K>> merge(T entity, Function<? super T, ? extends K> resourceConverterFn) {
        return supplyAsync(() -> wrap(em -> {
            Optional<T> merged = merge(em, entity);
            K resource = null;
            if (merged.isPresent()){
                T savedEntity = merged.get();
                resource = resourceConverterFn.apply(savedEntity);
            }
            return Optional.ofNullable(resource);
        }), executionContext);
    }

    public Optional<T> merge(EntityManager em, T entity) {
        entity = em.merge(entity);
        return Optional.ofNullable(entity);
    }

    public List<T> list(EntityManager em, Class<T> clazz) {
        String name = clazz.getSimpleName();
        String alias = name.substring(0, 1).toLowerCase();
        return em.createQuery("select " + alias + " from " + name + " " + alias, clazz).getResultList();
    }

    public <K> CompletionStage<Optional<K>> get(Long id, Function<? super T, ? extends K> resourceConverterFn) {
        return supplyAsync(() -> wrap(em ->{
            Optional<T> entityOptional = lookup(em, id);
            K resource = null;
            if (entityOptional.isPresent()){
                T entity = entityOptional.get();
                resource = resourceConverterFn.apply(entity);
            }
            return Optional.ofNullable(resource);
        } ), executionContext);
    }

    public T findById(EntityManager em, Long id) {
        return em.find(clazz, id);
    }

    private Optional<T> lookup(EntityManager em, Long id) /*throws SQLException */{
        //throw new SQLException("Call this to cause the circuit breaker to trip");
        return Optional.ofNullable(em.find(clazz, id));
    }
}
