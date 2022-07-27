package beans;

import models.Instituicao;
import models.Usuario;
import search.SearchField;
import search.SearchFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@SearchField(field = "nome")
public class UsuarioFilter extends SearchFilter<Usuario> {
    public Instituicao instituicao;

    @Override
    public Predicate predicate(CriteriaBuilder cb, Root<Usuario> root){
        Predicate predicate  = super.predicate(cb, root);
        Predicate instituicaoPredicate = cb.equal(root.get("instituicao"), instituicao);
        return cb.and(predicate, instituicaoPredicate);
    }
}
