package models;

import beans.InstituicaoResource;

import javax.persistence.*;

@Entity
public class Instituicao {
    @Id
    @SequenceGenerator(name = "seq_instituicao", sequenceName = "seq_instituicao", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_instituicao")
    public Long id;

    public String nome;

    @Embedded
    public Endereco endereco = new Endereco();

    public InstituicaoResource toResource(){
        return new InstituicaoResource(this.getId(), this.getNome());
    }

    public Instituicao() {
    }

    public Instituicao(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Instituicao(Long id) {
        this.id = id;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}


