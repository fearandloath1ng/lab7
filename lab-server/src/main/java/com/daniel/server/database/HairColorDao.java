package com.daniel.server.database;

import com.daniel.common.person.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class HairColorDao {

    public static Color getHairColorById(int id) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from color where id = ?")) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Color.valueOf(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Weapon was not found!");
    }

    public static int getHairColorIdByName(String name) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select id from color where name = ?")) {
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("View was not found!");
    }
}
