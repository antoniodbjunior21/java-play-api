package repository;

import models.Pessoa;
import play.db.jpa.JPAApi;

import javax.inject.Inject;

public class JPAPessoaRepository extends JPABaseRepository<Pessoa> implements PessoaRepository {

    @Inject
    public JPAPessoaRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext,Pessoa.class);
    }
}
