package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository extends AbstractJdbcRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final UserWithRolesExtractor userWithRolesExtractor;

    @Autowired
    public JdbcUserRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            UserWithRolesExtractor userWithRolesExtractor
    ) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.userWithRolesExtractor = userWithRolesExtractor;
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

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
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, roles.get(i).toString());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                }
        );

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
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, ur.role AS roles
                FROM users u
                LEFT JOIN user_roles ur ON u.id=ur.user_id
                WHERE id=?
                """, userWithRolesExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, ur.role AS roles
                FROM users u
                LEFT JOIN user_roles ur ON u.id=ur.user_id
                WHERE email=?
                """, userWithRolesExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, ur.role AS roles
                FROM users u
                LEFT JOIN user_roles ur ON u.id=ur.user_id
                ORDER BY name, email
                """, userWithRolesExtractor);
    }
}
