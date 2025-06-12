package modelos;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class Artigo implements Avaliavel {
    private int idartigo;
    private Usuario autor;
    private String tituloartigo;
    private Categorias categoria;
    private List<Topico> topicos;
    private Date dtultimaalteracao;
    private List<Usuario> editores;
    private List<Comentario> avaliacoes;

    // Construtor com mais de um tópico
    public Artigo(Usuario autor, String tituloartigo, Categorias categoria, List<Topico> topicos, Date dtultimaalteracao) {
        this.tituloartigo = tituloartigo;
        this.categoria = categoria;
        this.topicos = new ArrayList<Topico>();
        this.topicos.addAll(topicos);
        this.autor = autor;
        this.dtultimaalteracao = dtultimaalteracao;
    }

    // Construtor para um tópico só
    public Artigo(Usuario autor, String tituloartigo, Categorias categoria, Topico topico, Date dtultimaalteracao) {
        this.autor = autor;
        this.tituloartigo = tituloartigo;
        this.categoria = categoria;
        this.topicos = new ArrayList<Topico>();
        this.topicos.add(topico);
        this.dtultimaalteracao = dtultimaalteracao;
    }

    // Construtor para CRUD
    public Artigo(int idartigo, String tituloartigo, Categorias categoria, Date dtultimaalteracao) {
        this.idartigo = idartigo;
        this.tituloartigo = tituloartigo;
        this.categoria = categoria;
        this.dtultimaalteracao = dtultimaalteracao;
        this.topicos = new ArrayList<Topico>();
        this.editores = new ArrayList<Usuario>();
        this.avaliacoes = new ArrayList<Comentario>();
    }

    // Construtor para CRUD
    public Artigo(Usuario autor, int idArtigo, String tituloartigo, Categorias categoria, Date dtultimaalteracao) {
        this.autor = autor;
        this.idartigo = idArtigo;
        this.tituloartigo = tituloartigo;
        this.categoria = categoria;
        this.dtultimaalteracao = dtultimaalteracao;
        this.topicos = new ArrayList<Topico>();
        this.editores = new ArrayList<Usuario>();
        this.avaliacoes = new ArrayList<Comentario>();
    }

    public List<Comentario> getAvaliacoes() {
        return avaliacoes;
    }

    public int getIdArtigo() {
        return idartigo;
    }

    public void setIdArtigo(int idartigo) {
        this.idartigo = idartigo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public Categorias getCategoria() { return categoria; }

    public java.sql.Date getDtUltimaAlteracao() { return dtultimaalteracao; }

    public List<Topico> getTopicos() { return topicos; }

    public String getTituloArtigo() { return tituloartigo; }

    public void setTituloArtigo (String artigo) {
        this.tituloartigo = artigo;
    }

    public void adicionarEditor(Usuario editor) {
        if (editores == null) {
            editores = new ArrayList<Usuario>();
            editores.add(editor);
            return;
        }

        editores.add(editor);
    }

    public void adicionarAvaliacao(Comentario comentario) {
        if (avaliacoes == null) {
            avaliacoes = new ArrayList<Comentario>();
            avaliacoes.add(comentario);
        } else { avaliacoes.add(comentario); }
    }

    @Override
    public int getQtdAvaliacoes() {
        return avaliacoes.size();
    }

    @Override
    public float getMediaAvaliacoes() {
        int total = 0;

        for (Comentario avaliacao : avaliacoes) {
            total += avaliacao.getNota();
        }
        return (float) total / this.getQtdAvaliacoes();
    }

    public void removerTopico(Usuario editor, Topico topico) {
        topicos.remove(topico);
        adicionarEditor(editor);
    }

    public void adicionarTopico(Usuario editor, Topico topico) {
        topicos.add(topico);
        adicionarEditor(editor);
    }

    public void editarTopico(Usuario editor, Topico topicoeditado, String novotitulo, String novotexto) {
        for (Topico topico : topicos) {
            if (topico.equals(topicoeditado)) {
                topico.editarNomeTopico(novotitulo);
                topico.editarTextoTopico(novotexto);
                adicionarEditor(editor);
            }
        }
    }

    public void imprimirEditores() {
        if (editores == null)
            return;

        System.out.print("Editores: ");
        for (Usuario editor : editores) {
            if (editores.getLast().equals(editor)) {
                System.out.println(editor.getNomeUsuario());
                break;
            }

            System.out.print(editor.getNomeUsuario() + ", ");
        }
    }

    public void imprimirTopicos() {
        for (Topico topico : topicos) {
            if (topicos.getLast().equals(topico)) {
                System.out.println("Título tópico: " + topico.getNomeTopico());
                System.out.println("\"" + topico.getTextoTopico() + "\" \n");
                break;
            }

            System.out.println("Título tópico: " + topico.getNomeTopico());
            System.out.println("\"" + topico.getTextoTopico() + "\"");
        }
    }

    public void imprimirArtigo() {
        System.out.println("Título: " + tituloartigo);
        System.out.println("Categoria: " + categoria);
        System.out.println("Última alteração: " + dtultimaalteracao.toString());
        System.out.println("Autor: " + autor.getNomeUsuario());
        imprimirEditores(); imprimirTopicos();
    }

}
