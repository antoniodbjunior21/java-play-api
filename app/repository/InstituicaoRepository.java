package repository;

import beans.InstituicaoResource;
import com.google.inject.ImplementedBy;
import models.Instituicao;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAInstituicaoRepository.class)
public interface InstituicaoRepository extends BaseRepository<Instituicao>{
    CompletionStage<List<InstituicaoResource>> findAllByNome(String nome);
}
