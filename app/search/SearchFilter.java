package search;

import org.h2.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SearchFilter<T> {
    public String query;
    public Integer page;
    public Integer maxResults;
    public SearchSort<T> sort;

    public SearchFilter() {
    }

    public void select(CriteriaQuery<T> cq, Root<T> root){
        cq.select(root);
    }

    public void orderBy(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root){
        if (this.sort != null){
            if ("asc".equals(this.sort.direction)){
                cq.orderBy(cb.asc(root.get(this.sort.field)));
            }else if ("desc".equals(this.sort.direction)){
                cq.orderBy(cb.desc(root.get(this.sort.field)));
            }
        }
    }

    public Predicate predicate(CriteriaBuilder cb, Root<T> root){
        boolean annotationPresent = this.getClass().isAnnotationPresent(SearchField.class);

        SearchField searchField = null;
        if (annotationPresent) {
            searchField = this.getClass().getAnnotation(SearchField.class);
        }

        if (searchField != null) {
            if (!StringUtils.isNullOrEmpty(this.query)) {
                String field = searchField.field();
                return cb.like(cb.upper(root.get(field)), "%"+this.query.toUpperCase()+"%");
            }
        }
        return cb.conjunction();
    }
    private List<T> getList(EntityManager em, Class<T> clazz){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        select(cq, root);

        Predicate predicate = predicate(cb, root);
        cq.where(predicate);

        orderBy(cb, cq, root);

        TypedQuery<T> q = em.createQuery(cq);

        q.setFirstResult((this.page) * this.maxResults);
        q.setMaxResults(this.maxResults);

        return q.getResultList();
    }
    private Long count(EntityManager em, Class<T> clazz){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(clazz);
        cq.select(cb.count(root));
        Predicate predicate = predicate(cb, root);
        cq.where(predicate);
        return em.createQuery(cq).getSingleResult();
    }

    public <K> SearchResult<K> createQuery(EntityManager em, Class<T> clazz, Function<? super T, ? extends K> beanConverter){
        List<T> list = getList(em, clazz);
        List<K> beans = list.stream().map(beanConverter).collect(Collectors.toList());
        Long count = count(em, clazz);
        return new SearchResult<>(beans, count, this.maxResults);
    }
    public SearchResult<T> createQuery(EntityManager em, Class<T> clazz){
        List<T> list = getList(em, clazz);
        Long count = count(em, clazz);
        return new SearchResult<>(list, count, this.maxResults);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public SearchSort<T> getSort() {
        return sort;
    }

    public void setSort(SearchSort<T> sort) {
        this.sort = sort;
    }
}
