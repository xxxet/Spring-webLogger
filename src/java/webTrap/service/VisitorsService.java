package webTrap.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("visitorService")
@Transactional
public class VisitorsService {
//TODO handle exceptions, @transactional
    private NamedParameterJdbcTemplate jdbcTemplate;
    public final static String SQL_SELECT_ALL = "select ip, to_char(date,'YYYY-MM-DD hh24:mm:ss tz') as date, post, useragent from visitors order by date desc";
    public final static String SQL_SELECT_ONE = "select ip, to_char(date,'YYYY-MM-DD hh24:mm:ss tz') as date, post, useragent from visitors where  ip=:ip";
    public final static String SQL_UPDATE = "update visitors set date=now(), post=:post where ip=:ip";
    public final static String SQL_UPDATE_USERAGENT = "update visitors set date=now(), useragent=:useragent where ip=:ip";
    public final static String SQL_DELETE = "delete * from visitors";
    public final static String SQL_INSERT = "insert into visitors (ip, date, useragent) values (:ip, now(), :useragent)";
  
    // Maps a SQL result to a Java object
    private  RowMapper<Visitor> mapper = new RowMapper<Visitor>() {
            @Override
            public Visitor mapRow(ResultSet rs, int rowNum) throws SQLException {
                Visitor visitor = new Visitor();
                visitor.setIp(rs.getString("ip"));
                visitor.setDate(rs.getString("date"));
                visitor.setPost(rs.getString("post"));
                visitor.setUserAgent(rs.getString("useragent"));
                return visitor;
            }
        };
    
    @Resource(name="dataSource")
    public void setDataSource(DataSource dataSource) {
        System.out.println("setting DataSource");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Visitor> getAll() {
        System.out.println("Retrieving all visitors");
        // Retrieve all
        return jdbcTemplate.getJdbcOperations().query(SQL_SELECT_ALL, mapper);
    }

    public Visitor get(String ip) {
        System.out.println("Retrieving visitor: " + ip);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ip", ip);
        return jdbcTemplate.queryForObject(SQL_SELECT_ONE, parameters, mapper);
        //   return jdbcTemplate.queryForObject(SQL_SELECT_ONE, Visitor.class, parameters);
    }

    public boolean add(String ip, String useragent) {
        System.out.println("Adding new visitor: " + ip);

        // Assign values to parameters
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ip", ip);
        parameters.put("useragent", useragent);

        // Save
        try {
            jdbcTemplate.update(SQL_INSERT, parameters);
        } catch (DataAccessException dae) {
            System.out.println("add(): false " + ip);
            return false;
        }
        return true;
    }

    public void editPost(String ip, String post) {
        System.out.println("Editing existing visitor: " + ip);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ip", ip);
        parameters.put("post", post);
        jdbcTemplate.update(SQL_UPDATE, parameters);
    }
    
     public void editUserAgent(String ip, String useragent) {
        System.out.println("Editing existing visitor: " + ip);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ip", ip);
        parameters.put("useragent", useragent);
        jdbcTemplate.update(SQL_UPDATE_USERAGENT, parameters);
    }
     

    public void deleteAll() {
        System.out.println("Deleting all visitors");
        // Delete
        jdbcTemplate.getJdbcOperations().update(SQL_DELETE);
    }
}
