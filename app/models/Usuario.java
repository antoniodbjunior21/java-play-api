package models;

import beans.UsuarioResource;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class Usuario {

    @Id
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    public Long id;

    @NotEmpty
    public String nome;

    @Column(name = "email")
    public String email;
    public String senha;
    public Boolean administrador;
    public Boolean master;

    private Boolean ativo = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituicao_id")
    public Instituicao instituicao;

    @Embedded
    public Endereco endereco = new Endereco();

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(Long id, @NotEmpty String nome, String email, String senha, Boolean administrador, Boolean master, Boolean ativo, Instituicao instituicao, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.administrador = administrador;
        this.master = master;
        this.ativo = ativo;
        this.instituicao = instituicao;
        this.endereco = endereco;
    }

    public Usuario fromResource(UsuarioResource resource, Instituicao instituicao){
        return new Usuario(
                resource.id,
                resource.nome,
                resource.email,
                resource.senha,
                Boolean.TRUE.equals(resource.administrador),
                Boolean.TRUE.equals(resource.master),
                Boolean.TRUE.equals(resource.ativo),
                instituicao,
                null
                );
    }
    public UsuarioResource toResource(){
        return new UsuarioResource(
                this.id,
                this.nome,
                this.email,
                this.administrador,
                this.instituicao,
                this.master);
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
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

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }
}
