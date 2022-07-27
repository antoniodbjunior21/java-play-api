package beans;

import models.Instituicao;

public class InstituicaoResource {
    public Long id;
    public String nome;
    public String imagem;

    public InstituicaoResource() {
    }

    public InstituicaoResource(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Instituicao toInstituicao(){
        return new Instituicao(this.id, this.nome);
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
