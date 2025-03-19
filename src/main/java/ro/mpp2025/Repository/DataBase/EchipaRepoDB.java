package ro.mpp2025.Repository.DataBase;

import ro.mpp2025.Domain.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.Domain.Echipa;
import ro.mpp2025.Repository.IRepositoryEchipa;
import ro.mpp2025.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class EchipaRepoDB implements IRepositoryEchipa {

    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public EchipaRepoDB(Properties props) {
        logger.info("Initializing OrganizatorRepoDB with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Echipa> findOne(Integer nr) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Echipa where id_echipa = ?")) {
            preStmt.setInt(1, nr);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_echipa");
                    String name = result.getString("name");
                    Echipa echipa = new Echipa(name);
                    echipa.setId(id);
                    return Optional.of(echipa);
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
    public Iterable<Echipa> findAll() {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        List<Echipa> org = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Echipa")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_echipa");
                    String name = result.getString("name");
                    Echipa echipa = new Echipa(name);
                    echipa.setId(id);
                    org.add(echipa);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(org);
        return org;

    }

    @Override
    public Optional<Echipa> save(Echipa entity) {
        logger.trace(entity);
        String sql = "insert into Echipa(name) values (?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                ps.setInt(1, id);
                ps.setString(2, entity.getName());
            }
            ps.setString(1, entity.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<Echipa> delete(Echipa entity) {
        logger.trace(entity);
        String sql = "delete from Echipa where id_echipa = ?";
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
    public Optional<Echipa> update(Echipa new_entity) {
        logger.trace(new_entity);
        String sql = "update Echipa set name = ? where id_echipa = ?";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, new_entity.getName());
            ps.setInt(2, new_entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(new_entity);
    }
}

