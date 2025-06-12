package dao;

import modelos.Artigo;
import modelos.Categorias;
import modelos.Topico;
import modelos.Usuario;

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
            String sql = "INSERT INTO Usuario ( nomeUsuario, dtNascimento, emailUsuario, senhaUsuario, avaliacao ) VALUES (?, ?, ?, ?)";

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

                        for (Artigo artigo : usuario.getArtigosCriados()) {
                            cadastrarAutorias(usuario, artigo);
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
            String sql = "SELECT idUsuario, nomeUsuario, dtNascimento, emailUsuario FROM Usuario WHERE idUsuario = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, idUsuario);

                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int identificador = rst.getInt("idUsuario");
                    String nome = rst.getString("nomeUsuario");
                    String email = rst.getString("emailUsuario");
                    Date dtNascimento = rst.getDate("dtNascimento");
                    return new Usuario(identificador, nome, email, dtNascimento);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return null;
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
    public void excluir(int id) {

    }
}
