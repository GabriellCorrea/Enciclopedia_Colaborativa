package modelos;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class PaginaPrincipal extends Pagina implements Pesquisa {
    private List<Artigo> artigos;

    public PaginaPrincipal() { ; }

    public PaginaPrincipal(List<Artigo> artigos) {
        if (!(artigos.isEmpty())) {
            this.artigos = new ArrayList<Artigo>();
            this.artigos.addAll(artigos);
        }
    }

    public List<Artigo> getArtigos() {
        return artigos;
    }

    public int getId() { return idpagina; }

    public void setId(int id) { idpagina = id; }

    @Override
    public void exibir() {
        int cont = 1;
        for (Artigo artigo : artigos) {
            System.out.println("\n" + cont + ". ");
            System.out.println("Título: " + artigo.getTituloArtigo());
            System.out.println("Autor: " + artigo.getAutor().getNomeUsuario());
            cont++;
        }
    }

    @Override
    public List<Artigo> buscarPorAutor(String nomeAutor) {
        List<Artigo> retorno = new ArrayList<Artigo>();

        for (Artigo artigo : artigos) {
            if (artigo.getAutor().getNomeUsuario().equals(nomeAutor))
                retorno.add(artigo);
        }

        return retorno;
    }

    @Override
    public List<Artigo> buscarPorCategoria(String nomeCategoria) {
        List<Artigo> retorno = new ArrayList<Artigo>();

        for (Artigo artigo : artigos) {
            if (artigo.getCategoria().getNomeCategoria().equals(nomeCategoria))
                retorno.add(artigo);
        }

        return retorno;
    }

    @Override
    public List<Artigo> buscarPorNomeArtigo(String nomeArtigo) {
        List<Artigo> retorno = new ArrayList<Artigo>();

        for (Artigo artigo : artigos) {
            if (artigo.getTituloArtigo().contains(nomeArtigo))
                retorno.add(artigo);
        }

        return retorno;
    }
}