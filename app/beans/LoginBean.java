package beans;

import java.util.HashMap;
import java.util.Map;

public class LoginBean {
    public String password;
    public String email;
    public String authToken;
    public InstituicaoResource instituicao;
    public Boolean administrador;
    public String tokenUsuario;
    public String tokenInstituicao;
    public Map<String, String> sessionValues = new HashMap<>();

    public LoginBean() {
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public Map<String, String> getSessionValues() {
        return sessionValues;
    }

    public void setSessionValues(Map<String, String> sessionValues) {
        this.sessionValues = sessionValues;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public InstituicaoResource getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(InstituicaoResource instituicao) {
        this.instituicao = instituicao;
    }
}
