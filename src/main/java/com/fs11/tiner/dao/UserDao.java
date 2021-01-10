package com.fs11.tiner.dao;


import com.fs11.tiner.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private final Optional<Connection> connection;

    public UserDao(Optional<Connection> conn) {
        connection = conn;
    }

    public List<User> findAllProfiles() {
        List<User> userList = new ArrayList<>();
        connection.map(c -> {
            try {
                PreparedStatement ps = c.prepareStatement("select * from USER");
                ResultSet rset = ps.executeQuery();
                while (rset.next()) {
                    final Long id = rset.getLong(1);
                    final String name = rset.getString(2);
                    final String imgUrl = rset.getString(3);

                    userList.add(new User(id, name, imgUrl));

                    System.out.println(userList);
                }
                return c;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return Optional.empty();
            }
        });

        return userList;
    }

    public Optional<User> findNext(Integer currentIndex) {
        final List<User> profiles = findAllProfiles();

        if (currentIndex > profiles.size() - 1) return Optional.empty();
        return Optional.of(profiles.get(currentIndex));
    }

    public Optional<User> findByLogin(String name) {
        return connection.flatMap(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("select * from USER where name=(?)");
                ps.setString(1, name);

                ResultSet rset = ps.executeQuery();

                if (rset.next()) {
                    return getUser(rset);
                } else return Optional.empty();
            } catch (SQLException ex) {
                return Optional.empty();
            }
        });
    }

    public Optional<User> findById(Long userId) {
        return connection.flatMap(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("select * from USER where id=(?)");
                ps.setLong(1, userId);
                ResultSet rset = ps.executeQuery();

                if (rset.next()) {
                    return getUser(rset);
                }

                return Optional.empty();
            } catch (SQLException ex) {
                return Optional.empty();
            }
        });
    }

    public Optional<User> getUnvotedUser(Long userId) {
        return connection.flatMap(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("select * from USER u where NOT EXISTS (select * from VOTE where to_id = u.id and from_id = (?)) limit 1");
                ps.setLong(1, userId);
                ResultSet rset = ps.executeQuery();

                if (rset.next()) {
                    return getUser(rset);
                }
                return Optional.empty();
            } catch (SQLException ex) {
                return Optional.empty();
            }
        });
    }

    public void vote(Long fromUser, Long toUser, Boolean isLike)  {
        connection.flatMap(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("insert into VOTE (from_id, to_id, isLike) values (?, ?, ?)");
                ps.setLong(1, fromUser);
                ps.setLong(2, toUser);
                ps.setBoolean(3, isLike);
                ResultSet rset = ps.executeQuery();

                if (rset.next()) {
                    return getUser(rset);
                }
                return Optional.empty();
            } catch (SQLException ex) {
                return Optional.empty();
            }
        });
    }

    public List<User> getLiked(Long userId)  {
        List<User> liked = new ArrayList<>();
        connection.flatMap(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("select * from USER u where u.id IN (select (to_id) from VOTE where from_id = (?) and isLike = 1)");
                ps.setLong(1, userId);
                ResultSet rset = ps.executeQuery();

                while (rset.next()) {
                    liked.add(getUser(rset).get());
                }
                return Optional.empty();
            } catch (SQLException ex) {
                return Optional.empty();
            }
        });

        return liked;
    }

    private Optional<User> getUser(ResultSet rset) throws SQLException {
        final Long id = rset.getLong(1);
        final String user_name = rset.getString(2);
        final String img_url = rset.getString(3);
        final String password = rset.getString(4);
        return Optional.of(new User(id, user_name, img_url, password));
    }


}
