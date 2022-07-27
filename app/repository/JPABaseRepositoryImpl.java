package repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import play.db.jpa.JPAApi;

@Component
public abstract class JPABaseRepositoryImpl<T> extends JPABaseRepository<T>{

    @Autowired()
    public final void setJpaApi(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }
    @Autowired()
    public final void setExecutionContext(DatabaseExecutionContext executionContext){
        this.executionContext = executionContext;
    }

    public JPABaseRepositoryImpl(Class<T> typeParameterClass) {
        super(typeParameterClass);
    }
}
