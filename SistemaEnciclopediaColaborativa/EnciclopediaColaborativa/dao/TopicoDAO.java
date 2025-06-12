package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import modelos.Artigo;
import modelos.Topico;

public class TopicoDAO implements BaseDAO{

    private Connection connection;

    public TopicoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Topico topico, Artigo artigo) {
        try {
            String sql = "INSERT INTO Topico (nomeTopico, textoTopico, idArtigo) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.setString(1, topico.getNomeTopico());
                pstm.setString(2, topico.getTextoTopico());
                pstm.setInt(3, artigo.getIdArtigo());

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        topico.setIdTopico(rst.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void salvar(Object objeto) {

    }

    @Override
    public Object buscarPorId(int id) { return null; }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() { return null; }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() { return null; }

    public ArrayList<Topico> listarTodos(Artigo artigo) {

        ArrayList<Topico> topicos = new ArrayList<Topico>();

        try {
            String sql = "SELECT idTopico, nomeTopico, textoTopico FROM topico WHERE idArtigo = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, artigo.getIdArtigo());
                pstm.execute();
                ResultSet rst = pstm.getResultSet();
                while (rst.next()) {
                    int idTopico = rst.getInt("idTopico");
                    String nomeTopico = rst.getString("nomeTopico");
                    String textoTopico = rst.getString("textoTopico");
                    int idArtigo = rst.getInt("idArtigo");
                    Topico topico = new Topico(idTopico, nomeTopico, textoTopico, idArtigo);
                    topicos.add(topico);
                }
            }
            return topicos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Topico)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Pessoa.");
        }

        Topico topico = (Topico) objeto;

        String sql = "UPDATE Topico SET nomeTopico = ?, textoTopico = ?, idArtigo = ? WHERE idTopico = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, topico.getNomeTopico());
            pstm.setString(2, topico.getTextoTopico());
            pstm.setInt(3, topico.getIdTopico());

            int linhasAfetadas = pstm.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar topico: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM Topico WHERE idTopico = ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, id);

                int linhasAfetadas = pstm.executeUpdate();

                if (linhasAfetadas == 0) {
                    throw new SQLException("Falha ao deletar: nenhuma linha foi afetada.");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}