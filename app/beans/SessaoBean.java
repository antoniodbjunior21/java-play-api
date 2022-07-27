package beans;

import models.Instituicao;
import models.Usuario;

public class SessaoBean {
    public String token;
    public UsuarioResource usuario;
    public InstituicaoResource instituicao;
    public SessaoBean() {
    }

    public SessaoBean(String token, Usuario usuario, Instituicao instituicao) {
        this.token = token;
        if (usuario != null){
            this.usuario = usuario.toResource();
        }
        if (instituicao != null){
            this.instituicao = instituicao.toResource();
        }
    }
}
