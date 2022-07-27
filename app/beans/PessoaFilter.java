package beans;

import models.Instituicao;
import models.Pessoa;
import search.SearchField;
import search.SearchFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@SearchField(field = "nome")
public class PessoaFilter extends SearchFilter<Pessoa> {

    public Instituicao instituicao;

    @Override
    public Predicate predicate(CriteriaBuilder cb, Root<Pessoa> root){
        Predicate predicate  = super.predicate(cb, root);
        Predicate instituicaoPredicate = cb.equal(root.get("instituicao"), instituicao);
        return cb.and(predicate, instituicaoPredicate);
    }
}
