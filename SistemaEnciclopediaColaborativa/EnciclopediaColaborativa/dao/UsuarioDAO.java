package dao;

import modelos.*;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;

public class UsuarioDAO implements BaseDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) { this.connection = connection; }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario usuario = (Usuario) objeto;

        try {
            String sql = "INSERT INTO Usuario ( nomeUsuario, dtNascimento, emailUsuario, senhaUsuario, avaliacao ) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setString(1, usuario.getNomeUsuario());
                pstm.setDate(2, usuario.getDtNascimento());
                pstm.setString(3, usuario.getEmailUsuario());
                pstm.setString(4, usuario.getSenhaUsuario());
                pstm.setFloat(5, usuario.getMediaAvaliacoes());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        usuario.setIdUsuario(rst.getInt(1));

                        if (!(usuario.getArtigosCriados() == null)) {
                            for (Artigo artigo : usuario.getArtigosCriados()) {
                                if (!(artigo == null))
                                    cadastrarAutorias(usuario, artigo);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cadastrarAutorias(Usuario usuario, Artigo artigo) {
        try {
            String sql = "INSERT INTO Escreve (idArtigo, idUsuario) VALUES (?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {

                pstm.setInt(1, artigo.getIdArtigo());
                pstm.setInt(2, usuario.getIdUsuario());

                pstm.execute();
            }
            } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public Object buscarPorId(int idUsuario) {
        try {
            String sql = "SELECT idUsuario, nomeUsuario, dtNascimento, avaliacao, emailUsuario, senhaUsuario FROM Usuario WHERE idUsuario = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idUsuario);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idUsuario");
                    String nome = rst.getString("nomeUsuario");
                    String email = rst.getString("emailUsuario");
                    float mediaAvaliacao = rst.getFloat("avaliacao");
                    String senha = rst.getString("senhaUsuario");
                    Date dtNascimento = rst.getDate("dtNascimento");
                    return new Usuario(identificador, nome, mediaAvaliacao, email, senha, dtNascimento);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {

        ArrayList<Object> usuarios = new ArrayList<>();

        try {
            String sql = "SELECT idUsuario, nomeUsuario, avaliacao, emailUsuario, senhaUsuario, dtNascimento FROM Usuario";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idUsuario = rst.getInt("idUsuario");
                    String nomeUsuario = rst.getString("nomeUsuario");
                    float avaliacao = rst.getFloat("avaliacao");
                    String emailUsuario = rst.getString("emailUsuario");
                    String senhaUsuario = rst.getString("senhaUsuario");
                    Date dtNasc = rst.getDate("dtNascimento");
                    Usuario u = new Usuario(idUsuario, nomeUsuario, avaliacao, emailUsuario, senhaUsuario, dtNasc);
                    usuarios.add(u);
                }
            }
            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {

        ArrayList<Object> usuarios = new ArrayList<Object>();
        Usuario ultima = null; Artigo art = null;
        ArtigoDAO artDAO = new ArtigoDAO(connection);

        try {
            String sql = "SELECT usu.idUsuario, usu.nomeUsuario, usu.avaliacao, usu.emailUsuario, usu.dtNascimento, esc.idArtigo, art.tituloArtigo, art.categoria, art.dtUltimaMod "
                    + "FROM Usuario AS usu"
                    + "LEFT JOIN Escreve AS esc ON usu.idUsuario = esc.idUsuario"
                    + "LEFT JOIN Artigo AS art ON esc.idArtigo = art.idArtigo;";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    if (ultima == null || ultima.getIdUsuario() != rst.getInt(1)) {
                        int idUsuario = rst.getInt("idUsuario");
                        String nomeUsuario = rst.getString("nomeUsuario");
                        float avaliacao = rst.getFloat("avaliacao");
                        String emailUsuario = rst.getString("emailUsuario");
                        Date dtNasc = rst.getDate("dtNascimento");
                        Usuario u = new Usuario(idUsuario, nomeUsuario, avaliacao, emailUsuario, dtNasc);
                        usuarios.add(u);
                        ultima = u;
                    }

                    if (rst.getInt(7) != 0) {

                        int idArtigo = rst.getInt("idArtigo");
                        String tituloArtigo = rst.getString("tituloArtigo");
                        String categoria = rst.getString("categoria");
                        Date dtultimamod = rst.getDate("dtUltimaMod");
                        Artigo a = new Artigo(idArtigo, tituloArtigo, Categorias.getCategoriaPorString(categoria), dtultimamod);

                        art = (Artigo) artDAO.buscarPorId(idArtigo);
                        ultima.adicionarArtigo(art);
                    }

                }
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario usuario = (Usuario) objeto;

        String sql = "UPDATE Usuario SET nomeUsuario = ?, emailUsuario = ?, dtNascimento = ?, senhaUsuario = ? WHERE idUsuario = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, usuario.getNomeUsuario());
            pstm.setString(2, usuario.getEmailUsuario());
            pstm.setDate(3, usuario.getDtNascimento());
            pstm.setString(4, usuario.getSenhaUsuario());
            pstm.setInt(5, usuario.getIdUsuario());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int idUsuario) {
        try {
            String sql = "DELETE FROM Usuario WHERE idUsuario = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idUsuario);

                int linhasAfetadas = pstm.executeUpdate();

                if (linhasAfetadas == 0) {
                    throw new SQLException("Falha ao deletar: nenhuma linha foi afetada.");
                } }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
}

