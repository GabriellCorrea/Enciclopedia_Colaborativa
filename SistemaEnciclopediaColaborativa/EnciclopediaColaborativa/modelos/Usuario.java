package modelos;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class Usuario implements Avaliavel {
    private int idusuario;
    private String nomeusuario;
    private Date dtnascimento;
    private String emailusuario;
    private String senhausuario;
    private TipoUsuario tipousuario;
    private List<Artigo> artigoscriados;
    private List<Integer> avaliacoes;

    // Só pra teste
    public Usuario(String nome){
        this.nomeusuario = nome;
    }

    public Usuario(String nomeusuario, Date dtnascimento, String emailusuario, String senhausuario, TipoUsuario tipousuario) {
        this.nomeusuario = nomeusuario;
        this.dtnascimento = dtnascimento;
        this.emailusuario = emailusuario;
        this.senhausuario = senhausuario;
        this.tipousuario = tipousuario;
    }

    // Para CRUD
    public Usuario(int identificador, String nome, String email, Date dtNascimento) {
        this.idusuario = identificador;
        this.nomeusuario = nome;
        this.emailusuario = email;
        this.dtnascimento = dtNascimento;
    }

    public void adicionarAvaliacao(int avaliacao) {
        if (avaliacoes == null) {
            avaliacoes = new ArrayList<Integer>();
            avaliacoes.add(avaliacao);
        } else { avaliacoes.add(avaliacao); }
    }

    public void adicionarArtigo(Artigo artigo) {
        if (artigoscriados == null) {
            artigoscriados = new ArrayList<Artigo>();
            artigoscriados.add(artigo);
        } else { artigoscriados.add(artigo); }
    }

    @Override
    public int getQtdAvaliacoes() {
        return avaliacoes.size();
    }

    @Override
    public float getMediaAvaliacoes() {
        int total = 0;

        for (int avaliacao : avaliacoes) {
            total += avaliacao;
        }

        return (float) total / this.getQtdAvaliacoes();
    }

    public void alterarNome(String nomeusuario) {
        this.nomeusuario = nomeusuario;
    }

    public void alterarSenha(String senhausuario) {
        this.senhausuario = senhausuario;
    }

    public int getIdUsuario() {
        return idusuario;
    }

    public void setIdUsuario(int id) {
        this.idusuario = id;
    }

    public String getNomeUsuario() {
        return nomeusuario;
    }

    public Date getDtNascimento() {
        return dtnascimento;
    }

    public String getEmailUsuario() {
        return emailusuario;
    }

    public String getSenhaUsuario() {
        return senhausuario;
    }

    public TipoUsuario getTipoUsuario() {
        return tipousuario;
    }

    public List<Artigo> getArtigosCriados() {
        return artigoscriados;
    }

    public List<Integer> getAvaliacoes() {
        return avaliacoes;
    }
}
