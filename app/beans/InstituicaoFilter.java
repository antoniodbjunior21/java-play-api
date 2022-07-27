package beans;

import models.Instituicao;
import search.SearchFilter;
import search.SearchField;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@SearchField(field = "nome")
public class InstituicaoFilter extends SearchFilter<Instituicao> {
    public Long id;

    @Override
    public void select(CriteriaQuery<Instituicao> cq, Root<Instituicao> root){
        cq.multiselect(root.get("id"), root.get("nome"));
    }

    @Override
    public void orderBy(CriteriaBuilder cb, CriteriaQuery<Instituicao> cq, Root<Instituicao> root){
        super.orderBy(cb, cq, root);
        if (this.sort == null){
            cq.orderBy(cb.desc(root.get("id")));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
