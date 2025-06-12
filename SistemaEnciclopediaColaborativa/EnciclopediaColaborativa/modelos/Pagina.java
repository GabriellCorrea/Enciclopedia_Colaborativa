package modelos;

public abstract class Pagina {
    protected int idpagina;
    public abstract void exibir();

    public int getIdPagina() {
        return idpagina;
    }

    public void setIdPagina(int idpagina) {
        this.idpagina = idpagina;
    }

    public String compartilharLink() {
        return "www.sistemaenciclopedia.com/" + idpagina;
    }
}
