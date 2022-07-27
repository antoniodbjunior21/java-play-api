package repository;

import beans.LoginBean;
import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAAuthRepository.class)
public interface AuthRepository {
    CompletionStage<LoginBean> login(LoginBean bean);
}
