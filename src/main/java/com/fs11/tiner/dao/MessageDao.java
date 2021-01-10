package com.fs11.tiner.dao;

import com.fs11.tiner.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDao {
    private final Optional<Connection> connection;

    public MessageDao(Optional<Connection> conn) {
        connection = conn;
    }

    public List<Message> getMessages(Long firstUserId, Long secondUserId) {
        List<Message> messageList = new ArrayList<>();
        connection.map(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM MESSAGE m WHERE m.from_id=(?) and m.to_id=(?) or m.from_id=(?) and m.to_id=(?)");
                ps.setLong(1, firstUserId);
                ps.setLong(2, secondUserId);
                ps.setLong(3, secondUserId);
                ps.setLong(4, firstUserId);

                ResultSet rset = ps.executeQuery();

                while (rset.next()) {
                    final Long id = rset.getLong(1);
                    final Long date_in_millis = rset.getLong(2);
                    final Long from_id = rset.getLong(3);
                    final String message_body = rset.getString(4);
                    final Long to_id = rset.getLong(5);

                    messageList.add(new Message(id, date_in_millis, from_id, to_id, message_body));
                }
                return conn;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return Optional.empty();
            }
        });
        return messageList;
    }

    public Optional<Message> save(Message message) {
        return connection.flatMap(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement("insert into MESSAGE (date_in_millis, to_id, message_body, from_id) values (?, ?, ?, ?)");
                ps.setLong(1, message.getDate_in_millis());
                ps.setLong(2, message.getTo_id());
                ps.setString(3, message.getMessage_body());
                ps.setLong(4, message.getFrom_id());

                ResultSet rset = ps.executeQuery();

                if (rset.next()) {
                    final Long id = rset.getLong(1);
                    final Long date_in_millis = rset.getLong(2);
                    final Long from_id = rset.getLong(3);
                    final String message_body = rset.getString(4);
                    final Long to_id = rset.getLong(5);

                    return Optional.of(new Message(id, date_in_millis, from_id, to_id, message_body));
                }
                return Optional.empty();
            } catch (SQLException ex) {
                return Optional.empty();
            }
        });
    }
}
