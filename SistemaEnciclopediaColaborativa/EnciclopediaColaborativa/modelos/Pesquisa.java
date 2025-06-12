package modelos;

import java.util.List;

public interface Pesquisa {
    public List<Artigo> buscarPorNomeArtigo(String nomeArtigo);
    public List<Artigo> buscarPorCategoria(String nomeCategoria);
    public List<Artigo> buscarPorAutor(String nomeAutor);
}