package repository;

import beans.InstituicaoResource;
import models.Instituicao;
import play.db.jpa.JPAApi;
import utils.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAInstituicaoRepository extends JPABaseRepository<Instituicao> implements InstituicaoRepository {

    @Inject
    public JPAInstituicaoRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        super(jpaApi, executionContext, Instituicao.class);
    }

    public CompletionStage<List<InstituicaoResource>> findAllByNome(String nome){
        return supplyAsync(()-> wrap(entityManager -> {
            List<Instituicao> instituicaos = findAllByNome(entityManager, nome);
            return instituicaos.stream().map(Instituicao::toResource).collect(Collectors.toList());
        }), executionContext);
    }

    @SuppressWarnings("unchecked")
    public List<Instituicao> findAllByNome(EntityManager em, String nome){
        StringBuilder select = new StringBuilder(" SELECT ig FROM Instituicao ig ");

        if (Utils.isNotNullOrEmpty(nome)){
            select.append(" WHERE UPPER( ig.nome) LIKE :nome ");
        }
        Query query = em.createQuery(select.toString());
        if (Utils.isNotNullOrEmpty(nome)){
            query.setParameter("nome", "%"+nome.toUpperCase()+"%");
        }
        return query.getResultList();
    }
}
