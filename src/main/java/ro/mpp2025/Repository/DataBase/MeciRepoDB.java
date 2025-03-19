package ro.mpp2025.Repository.DataBase;

import ro.mpp2025.Domain.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.Domain.Echipa;
import ro.mpp2025.Domain.Meci;
import ro.mpp2025.Repository.IRepositoryEchipa;
import ro.mpp2025.Repository.IRepositoryMeci;
import ro.mpp2025.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MeciRepoDB implements IRepositoryMeci {

    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public MeciRepoDB(Properties props) {
        logger.info("Initializing OrganizatorRepoDB with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Meci> findOne(Integer nr) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        String sql = "SELECT M.id_meci, M.nume_meci, M.nr_loc, M.pret, e1.id_echipa , e1.name as echipaA_nume, e2.id_echipa , e2.name as echipaB_nume " +
                "FROM Meci M " +
                "JOIN Echipa e1 ON M.echipaA_id = e1.id_echipa " +
                "JOIN Echipa e2 ON M.echipaB_id = e2.id_echipa " +
                "WHERE M.id_meci = ?";
        try (PreparedStatement preStmt = con.prepareStatement(sql)) {
            preStmt.setInt(1, nr);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_meci");
                    Echipa echipaA = new Echipa(result.getString("echipaA_nume"));
                    Echipa echipaB = new Echipa(result.getString("echipaB_nume"));
                    String nume_meci = result.getString("nume_meci");
                    Integer nr_loc = result.getInt("nr_loc");
                    int pret = result.getInt("pret");
                    Meci meci = new Meci(echipaA, echipaB, nume_meci, nr_loc, pret);
                    meci.setId(id);
                    return Optional.of(meci);
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
    public Iterable<Meci> findAll() {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        List<Meci> meciuri = new ArrayList<Meci>();
        String sql = "SELECT M.id_meci, M.nume_meci, M.nr_loc, M.pret, e1.id_echipa , e1.name as echipaA_nume, e2.id_echipa , e2.name as echipaB_nume " +
                "FROM Meci M " +
                "JOIN Echipa e1 ON M.echipaA_id = e1.id_echipa " +
                "JOIN Echipa e2 ON M.echipaB_id = e2.id_echipa " ;
        try (PreparedStatement preStmt = con.prepareStatement(sql)) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_meci");
                    Echipa echipaA = new Echipa(result.getString("echipaA_nume"));
                    Echipa echipaB = new Echipa(result.getString("echipaB_nume"));
                    String nume_meci = result.getString("nume_meci");
                    Integer nr_loc = result.getInt("nr_loc");
                    int pret = result.getInt("pret");
                    Meci meci = new Meci(echipaA, echipaB, nume_meci, nr_loc, pret);
                    meci.setId(id);
                    meciuri.add(meci);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        logger.traceExit();
        return meciuri;

    }

    @Override
    public Optional<Meci> save(Meci entity) {
        System.out.println(entity);
        logger.trace(entity);
        String sql = "insert into Meci(echipaA_id, echipaB_id, nume_meci, nr_loc, pret) values (?,?,?,?,?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getEchipaA().getId());
            ps.setInt(2, entity.getEchipaB().getId());
            ps.setString(3, entity.getNume_meci());
            ps.setInt(4, entity.getNr_loc());
            ps.setInt(5, entity.getPret());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<Meci> delete(Meci entity) {
        logger.trace(entity);
        String sql = "delete from Meci where id_meci = ?";
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
    public Optional<Meci> update(Meci new_entity) {
        logger.trace(new_entity);
        String sql = "update Meci set echipaA_id = ? , echipaB_id = ? , nume_meci = ? , nr_loc = ? , pret = ? where id_meci = ?";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, new_entity.getEchipaA().getId());
            ps.setInt(2, new_entity.getEchipaB().getId());
            ps.setString(3, new_entity.getNume_meci());
            ps.setInt(4, new_entity.getNr_loc());
            ps.setInt(5, new_entity.getPret());
            ps.setInt(6, new_entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(new_entity);
    }

    @Override
    public Optional<Meci> findOneMeciByNume(String nume) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        String sql = "SELECT M.id_meci, M.nume_meci, M.nr_loc, M.pret, e1.id_echipa as echipaA_id, e1.name as echipaA_nume, e2.id_echipa as echipaB_id, e2.name as echipaB_nume " +
                "FROM Meci M " +
                "JOIN Echipa e1 ON M.echipaA_id = e1.id_echipa " +
                "JOIN Echipa e2 ON M.echipaB_id = e2.id_echipa " +
                "WHERE M.nume_meci = ?";
        try (PreparedStatement preStmt = con.prepareStatement(sql)) {
            preStmt.setString(1, nume);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_meci");
                    int id_echipaA = result.getInt("echipaA_id");
                    int id_echipaB = result.getInt("echipaB_id");
                    Echipa echipaA = new Echipa(result.getString("echipaA_nume"));
                    echipaA.setId(id_echipaA);
                    Echipa echipaB = new Echipa(result.getString("echipaB_nume"));
                    echipaB.setId(id_echipaB);
                    String nume_meci = result.getString("nume_meci");
                    Integer nr_loc = result.getInt("nr_loc");
                    int pret = result.getInt("pret");
                    Meci meci = new Meci(echipaA, echipaB, nume_meci, nr_loc, pret);
                    meci.setId(id);
                    return Optional.of(meci);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        logger.traceExit();
        return Optional.empty();
    }
}

