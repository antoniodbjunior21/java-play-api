package repository;

import com.google.inject.ImplementedBy;
import models.Pessoa;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAPessoaRepository.class)
public interface PessoaRepository extends BaseRepository<Pessoa>{

}
