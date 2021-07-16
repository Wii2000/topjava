package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserWithRolesExtractor implements ResultSetExtractor<List<User>> {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> users = new HashMap<>();
        User currentUser;
        int rowNum = 0;

        while (rs.next()) {
            currentUser = ROW_MAPPER.mapRow(rs, rowNum++);
            currentUser.setRoles(List.of(Role.valueOf(rs.getString("roles"))));
            Integer userId = currentUser.getId();
            users.merge(userId, currentUser, (oldOne, newOne) -> {
                User user = users.get(userId);
                Set<Role> role = oldOne.getRoles();
                role.addAll(newOne.getRoles());
                user.setRoles(role);
                return user;
            });
        }

        return new ArrayList<>(users.values());
    }
}
