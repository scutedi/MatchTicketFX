package ro.mpp2025.Repository.DataBase;

import ro.mpp2025.Domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.Repository.IRepositoryUser;
import ro.mpp2025.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class UserRepoDB implements IRepositoryUser {

    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils dbUtils;

    public UserRepoDB(Properties props) {
        logger.info("Initializing OrganizatorRepoDB with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Optional<User> findOne(Integer nr) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from User where id_user = ?"))
        {
            preStmt.setInt(1, nr);
            try(ResultSet result = preStmt.executeQuery())
            {
                if(result.next())
                {
                    int id = result.getInt("id_user");
                    String email = result.getString("email");
                    String parola = result.getString("password");
                    User organizator = new User(email, parola);
                    organizator.setId(id);
                    return Optional.of(organizator);
                }
            }
        }
        catch(SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();

        Connection con=dbUtils.getConnection();
        List<User> org = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from User"))
        {
            try(ResultSet result = preStmt.executeQuery())
            {
                while(result.next())
                {
                    int id = result.getInt("id_user");
                    String email = result.getString("email");
                    String parola = result.getString("password");
                    User organizator = new User(email, parola);
                    organizator.setId(id);
                    org.add(organizator);
                }
            }
        }
        catch(SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(org);
        return org;

    }

    @Override
    public Optional<User> save(User entity) {
        logger.trace(entity);
        String sql="insert into User(email, password) values (?,?)";
        Connection con=dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1,entity.getEmail());
            ps.setString(2,entity.getPassword());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<User> delete(User entity) {
        logger.trace(entity);
        String sql="delete from User where id_user = ?";
        Connection con=dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1,entity.getId());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<User> update(User new_entity) {
        logger.trace(new_entity);
        String sql="update organizatori set email = ?, password = ? where id_user = ?";
        Connection con=dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1,new_entity.getEmail());
            ps.setString(2,new_entity.getPassword());
            ps.setInt(3,new_entity.getId());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            logger.error(e);
            return Optional.empty();
        }
        logger.traceExit();
        return Optional.of(new_entity);
    }

    @Override
    public Optional<User> findOneUserByEmail(String email) {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from User where email = ?"))
        {
            preStmt.setString(1, email);
            try(ResultSet result = preStmt.executeQuery())
            {
                if(result.next())
                {
                    int id = result.getInt("id_user");
                    String parola = result.getString("password");
                    User organizator = new User(email, parola);
                    organizator.setId(id);
                    return Optional.of(organizator);
                }
            }
        }
        catch(SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }

        logger.traceExit();
        return Optional.empty();
    }

}
