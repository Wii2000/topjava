package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository extends AbstractJdbcRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> userWithRolesExtractor = rs -> {
        Map<Integer, User> users = new LinkedHashMap<>();
        User currentUser;
        String role;
        int rowNum = 0;

        while (rs.next()) {
            role = rs.getString("role");

            if ((currentUser = users.get(rs.getInt("id"))) != null) {
                currentUser.getRoles().add(Role.valueOf(role));
            } else {
                currentUser = ROW_MAPPER.mapRow(rs, rowNum);
                currentUser.setRoles(role != null ? EnumSet.of(Role.valueOf(role)) : EnumSet.noneOf(Role.class));
                users.put(currentUser.getId(), currentUser);
            }

            rowNum++;
        }

        return new ArrayList<>(users.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;


    @Autowired
    public JdbcUserRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                UPDATE users SET name=:name, email=:email, password=:password,
                registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay
                WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            namedParameterJdbcTemplate.update("DELETE FROM user_roles WHERE user_id=:id", parameterSource);
        }

        List<Role> roles = List.copyOf(user.getRoles());
        if (!roles.isEmpty()) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO user_roles (user_id, role) VALUES (?,?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, user.getId());
                            ps.setString(2, roles.get(i).name());
                        }

                        public int getBatchSize() {
                            return roles.size();
                        }
                    }
            );
        }

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, ur.role AS role
                FROM users u
                LEFT JOIN user_roles ur ON u.id=ur.user_id
                WHERE id=?
                """, userWithRolesExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, ur.role AS role
                FROM users u
                LEFT JOIN user_roles ur ON u.id=ur.user_id
                WHERE email=?
                """, userWithRolesExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, ur.role AS role
                FROM users u
                LEFT JOIN user_roles ur ON u.id=ur.user_id
                ORDER BY name, email
                """, userWithRolesExtractor);
    }
}
