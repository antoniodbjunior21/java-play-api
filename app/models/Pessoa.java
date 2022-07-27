package models;

import beans.PessoaResource;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @SequenceGenerator(name = "seq_pessoa", sequenceName = "seq_pessoa", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
    public Long id;

    @NotEmpty
    public String nome;

    @Column(name = "email")
    public String email;

    private Boolean ativo = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituicao_id")
    public Instituicao instituicao;

    @Embedded
    public Endereco endereco = new Endereco();

    public Pessoa() {
    }

    public Pessoa(Long id) {
        this.id = id;
    }

    public Pessoa(Long id, String nome, String email, Boolean ativo, Instituicao instituicao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
        this.instituicao = instituicao;
    }

    public Pessoa fromResource(PessoaResource resource, Instituicao instituicao){
        return new Pessoa(resource.id, resource.nome, resource.email, Boolean.TRUE, instituicao);
    }
    public PessoaResource toResource(){
        return new PessoaResource(this.id, this.nome, this.email, this.instituicao);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
