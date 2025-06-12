package dao;

import modelos.Artigo;
import modelos.PaginaPrincipal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PaginaPrincipalDAO implements BaseDAO {

    private Connection connection;

    public PaginaPrincipalDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {

        if (!(objeto instanceof PaginaPrincipal)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo PaginaPrincipal.");
        }

        PaginaPrincipal paginaPrincipal = (PaginaPrincipal) objeto;

        try {
            String sql = "INSERT INTO PaginaPrincipal VALUES ()";

            try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstm.execute();

                try (ResultSet rst = pstm.getGeneratedKeys()) {
                    while (rst.next()) {
                        paginaPrincipal.setId(rst.getInt(1));
/*                        for (Artigo artigo : paginaPrincipal.getArtigos()) {
                            alocarArtigo(paginaPrincipal, artigo);
                        }*/
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void alocarArtigo(PaginaPrincipal pg, Artigo art) {
        try {
            String sql = "INSERT INTO Artigo (idPaginaPrincipal) VALUES ( ? )";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {

                pstm.setInt(1, pg.getId());

                pstm.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object buscarPorId(int id) {
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() { return null; }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        /*CONTINUAR*/

        ArrayList<Object> artigos = new ArrayList<>();
        Artigo ultima = null;

        try {

            String sql = "SELECT pgp.idPaginaPrincipal, art.idArtigo, art.tituloArtigo, art.dtUltimaMod, usu.nomeUsuario AS nomeAutor "
                    + "FROM PaginaPrincipal AS pgp "
                    + "LEFT JOIN Contem AS cont ON pgp.idPaginaPrincipal = cont.idPaginaPrincipal "
                    + "LEFT JOIN Artigo AS art ON cont.idArtigo = art.idArtigo "
                    + "LEFT JOIN Escreve AS esc ON art.idArtigo = esc.idArtigo "
                    + "LEFT JOIN Usuario AS usu ON esc.idUsuario = usu.idUsuario;";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.execute();

                try (ResultSet rst = pstm.getResultSet()) {
                    while (rst.next()) {
                        if (ultima == null || ultima.getId() != rst.getInt(1)) {
                            int p_id = rst.getInt(1);
                            String nome = rst.getString(2);
                            String cpf = rst.getString(3);
                            LocalDate data = rst.getObject(4, LocalDate.class);
                            int idade = rst.getInt(5);
                            Pessoa p = new Pessoa(p_id, nome, cpf, data, idade);
                            pessoas.add(p);
                            ultima = p;
                        }

                        if (rst.getInt(6) != 0) {
                            int tel_id = rst.getInt(6);
                            TipoTelefone tipo = TipoTelefone.values()[rst.getInt(7)];
                            int cod_pais = rst.getInt(8);
                            int cod_area = rst.getInt(9);
                            int numero = rst.getInt(10);
                            Telefone t = new Telefone(tel_id, tipo, cod_pais, cod_area, numero);
                            ultima.addTelefone(t);
                        }
                    }
                }
                return pessoas;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Object objeto) { ; }

    @Override
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM PaginaPrincipal WHERE idPaginaPrincipal = ?";

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