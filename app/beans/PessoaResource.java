package beans;

import models.Instituicao;
import org.h2.util.StringUtils;

public class PessoaResource {
    public Long id;
    public String nome;
    public String primeiroNome;
    public String primeiroSegundoNome;
    public String segundoNome;
    public String email;
    public String imagem;
    public InstituicaoResource instituicao;
    public PessoaResource() {
    }
    public PessoaResource(Long id, String nome, String email, Instituicao instituicao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        processarAbreviaturasNomes();

        if (instituicao != null){
            this.instituicao = instituicao.toResource();
        }
    }

    private void processarAbreviaturasNomes() {
        if (StringUtils.isNullOrEmpty(this.nome)) {
            return;
        }
        String[] arr = this.nome.split(" ");
        this.primeiroNome = arr[0];
        this.primeiroSegundoNome = this.primeiroNome;
        if (arr.length > 1) {
            String segundoNome = arr[1];
            if (!StringUtils.isNullOrEmpty(segundoNome)) {
                if (segundoNome.equalsIgnoreCase("DE") || segundoNome.equalsIgnoreCase("DA")) {
                    segundoNome = "";
                    if (arr.length > 2) {
                        segundoNome = arr[2];
                    }
                }
            }
            if (!StringUtils.isNullOrEmpty(segundoNome)) {
                this.segundoNome = segundoNome;
                this.primeiroSegundoNome += " " + this.segundoNome;
            }
        }
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

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getPrimeiroSegundoNome() {
        return primeiroSegundoNome;
    }

    public void setPrimeiroSegundoNome(String primeiroSegundoNome) {
        this.primeiroSegundoNome = primeiroSegundoNome;
    }

    public String getSegundoNome() {
        return segundoNome;
    }

    public void setSegundoNome(String segundoNome) {
        this.segundoNome = segundoNome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public InstituicaoResource getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(InstituicaoResource instituicao) {
        this.instituicao = instituicao;
    }
}
