package beans;

public class LoginBean {
    public String senha;
    public String email;
    public String token;
    public InstituicaoResource instituicao;
    public Boolean administrador;
    public String tokenUsuario;
    public String tokenInstituicao;

    public LoginBean() {
    }

    public String getTokenUsuario() {
        return tokenUsuario;
    }

    public void setTokenUsuario(String tokenUsuario) {
        this.tokenUsuario = tokenUsuario;
    }

    public String getTokenInstituicao() {
        return tokenInstituicao;
    }

    public void setTokenInstituicao(String tokenInstituicao) {
        this.tokenInstituicao = tokenInstituicao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public InstituicaoResource getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(InstituicaoResource instituicao) {
        this.instituicao = instituicao;
    }
}
