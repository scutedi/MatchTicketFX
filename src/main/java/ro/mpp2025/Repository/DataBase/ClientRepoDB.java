package ro.mpp2025.Repository.DataBase;

import ro.mpp2025.Domain.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.Repository.IRepositoryClient;
import ro.mpp2025.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ClientRepoDB implements IRepositoryClient {

    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public ClientRepoDB(Properties props) {
        logger.info("Initializing OrganizatorRepoDB with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Client> findOne(Integer nr) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Client where id_client = ?")) {
            preStmt.setInt(1, nr);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_client");
                    String email = result.getString("nume");
                    String parola = result.getString("adresa");
                    Client client = new Client(email, parola);
                    client.setId(id);
                    return Optional.of(client);
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
    public Optional<Client> findOneClientByNumeAndAdresa(String name, String adresa) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Client where nume = ? and adresa = ?")) {
            preStmt.setString(1, name);
            preStmt.setString(2, adresa);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_client");
                    String email = result.getString("nume");
                    String parola = result.getString("adresa");
                    Client client = new Client(email, parola);
                    client.setId(id);
                    return Optional.of(client);
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
    public Iterable<Client> findAll() {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        List<Client> org = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Client")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_client");
                    String email = result.getString("nume");
                    String parola = result.getString("adresa");
                    Client client = new Client(email, parola);
                    client.setId(id);
                    org.add(client);
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
    public Optional<Client> save(Client entity) {
        logger.trace(entity);
        String sql = "insert into Client(nume, adresa) values (?,?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getAdresa());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<Client> delete(Client entity) {
        logger.trace(entity);
        String sql = "delete from Client where id_client = ?";
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
    public Optional<Client> update(Client new_entity) {
        logger.trace(new_entity);
        String sql = "update Client set nume = ?, adresa = ? where id_client = ?";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, new_entity.getNume());
            ps.setString(2, new_entity.getAdresa());
            ps.setInt(3, new_entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(new_entity);
    }
}

