package ro.mpp2025.Repository.DataBase;

import ro.mpp2025.Domain.Bilet;
import ro.mpp2025.Domain.Echipa;
import ro.mpp2025.Domain.Meci;
import ro.mpp2025.Domain.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.Repository.IRepository;
import ro.mpp2025.Repository.IRepositoryBilet;
import ro.mpp2025.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class BiletRepoDB implements IRepositoryBilet {

    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public BiletRepoDB(Properties props) {
        logger.info("Initializing BiletRepoDB with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Bilet> findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT b.bilet_id, m.meci_id, m.nume_meci, m.nr_loc, m.pret, " +
                "eA.echipa_id AS echipaA_id, eA.nume AS echipaA_nume, " +
                "eB.echipa_id AS echipaB_id, eB.nume AS echipaB_nume, " +
                "c.client_id, c.nume AS client_nume, c.adresa " +
                "FROM Bilet b " +
                "JOIN Meci m ON b.meci_id = m.meci_id " +
                "JOIN Echipa eA ON m.echipaA_id = eA.echipa_id " +
                "JOIN Echipa eB ON m.echipaB_id = eB.echipa_id " +
                "JOIN Client c ON b.client_id = c.client_id " +
                "WHERE b.bilet_id = ?";
        try (PreparedStatement preStmt = con.prepareStatement(sql)) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Echipa echipaA = new Echipa(result.getString("echipaA_nume"));
                    Echipa echipaB = new Echipa(result.getString("echipaB_nume"));
                    Meci meci = new Meci(echipaA, echipaB, result.getString("nume_meci") ,result.getInt("nr_loc"), result.getInt("pret"));
                    Client client = new Client(result.getString("client_nume"), result.getString("adresa"));
                    Bilet bilet = new Bilet(meci, client);
                    bilet.setId(result.getInt("bilet_id"));
                    return Optional.of(bilet);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Iterable<Bilet> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT b.bilet_id, m.id_meci, m.nume_meci, m.nr_loc, m.pret, " +
                "eA.id_echipa AS echipaA_id, eA.name AS echipaA_nume, " +
                "eB.id_echipa AS echipaB_id, eB.name AS echipaB_nume, " +
                "c.id_client, c.nume AS client_nume, c.adresa " +
                "FROM Bilet b " +
                "JOIN Meci m ON b.meci_id = m.id_meci " +
                "JOIN Echipa eA ON m.echipaA_id = eA.id_echipa " +
                "JOIN Echipa eB ON m.echipaB_id = eB.id_echipa " +
                "JOIN Client c ON b.client_id = c.id_client ";  // Asigură-te că filtrarea se face corect

        List<Bilet> bilets = new ArrayList<>();

        try (PreparedStatement preStmt = con.prepareStatement(sql)) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Echipa echipaA = new Echipa(result.getString("echipaA_nume"));
                    echipaA.setId(result.getInt("echipaA_id"));
                    Echipa echipaB = new Echipa(result.getString("echipaB_nume"));
                    echipaB.setId(result.getInt("echipaB_id"));
                    Meci meci = new Meci(echipaA, echipaB, result.getString("nume_meci"),
                            result.getInt("nr_loc"), result.getInt("pret"));
                    meci.setId(result.getInt("id_meci"));
                    Client client = new Client(result.getString("client_nume"), result.getString("adresa"));
                    Bilet bilet = new Bilet(meci, client);
                    bilet.setId(result.getInt("bilet_id"));
                    bilets.add(bilet);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return bilets;
    }

    @Override
    public Optional<Bilet> save(Bilet entity) {
        System.out.println(entity);
        logger.trace(entity);
        String sql = "insert into Bilet(meci_id, client_id) values (?,?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getMeci_id().getId());
            ps.setInt(2, entity.getClient_id().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<Bilet> delete(Bilet entity) {
        logger.trace(entity);
        String sql = "delete from Bilet where bilet_id = ?";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<Bilet> update(Bilet new_entity) {
        logger.trace(new_entity);
        String sql = "update Bilet set meci_id = ? , client_id = ? where bilet_id = ?";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, new_entity.getMeci_id().getId());
            ps.setInt(2, new_entity.getClient_id().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(new_entity);
    }


    @Override
    public Iterable<Bilet> findAllOneByName(String numeClient) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT b.bilet_id, m.id_meci, m.nume_meci, m.nr_loc, m.pret, " +
                "eA.id_echipa AS echipaA_id, eA.name AS echipaA_nume, " +
                "eB.id_echipa AS echipaB_id, eB.name AS echipaB_nume, " +
                "c.id_client, c.nume AS client_nume, c.adresa, COUNT(b.bilet_id) AS numar_bilete " +
                "FROM Bilet b " +
                "JOIN Meci m ON b.meci_id = m.id_meci " +
                "JOIN Echipa eA ON m.echipaA_id = eA.id_echipa " +
                "JOIN Echipa eB ON m.echipaB_id = eB.id_echipa " +
                "JOIN Client c ON b.client_id = c.id_client " +
                "WHERE c.nume = ? " +
                "GROUP BY c.nume, m.id_meci"; // Asigură-te că filtrarea se face corect

        List<Bilet> bilets = new ArrayList<>();

        try (PreparedStatement preStmt = con.prepareStatement(sql)) {
            preStmt.setString(1, numeClient);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Echipa echipaA = new Echipa(result.getString("echipaA_nume"));
                    echipaA.setId(result.getInt("echipaA_id"));
                    Echipa echipaB = new Echipa(result.getString("echipaB_nume"));
                    echipaB.setId(result.getInt("echipaB_id"));
                    Meci meci = new Meci(echipaA, echipaB, result.getString("nume_meci"),
                            result.getInt("nr_loc"), result.getInt("pret"));
                    meci.setId(result.getInt("id_meci"));
                    Client client = new Client(result.getString("client_nume"), result.getString("adresa"));
                    Bilet bilet = new Bilet(meci, client);
                    bilet.setId(result.getInt("bilet_id"));
                    bilets.add(bilet);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return bilets;
    }

}
