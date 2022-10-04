package com.daniel.server.database;

import com.daniel.common.person.Coordinates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class CoordinatesDao {

    private CoordinatesDao() {
    }
    public static Coordinates getCoordinatesById(int id) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        PreparedStatement ps = null;
        Coordinates coordinates = new Coordinates();
        try {
            ps = connection.prepareStatement("select * from coordinates where id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coordinates.setX(rs.getFloat("x"));
                coordinates.setY(rs.getLong("y"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    public static long createCoordinates(Coordinates coordinates) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO coordinates (x, y) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, coordinates.getX());
            ps.setFloat(2, coordinates.getY());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void updateCoordinates(Coordinates coordinates, int coordinatesId) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement("UPDATE coordinates set x = ?, y = ? WHERE id = ?");
            ps.setDouble(1, coordinates.getX());
            ps.setFloat(2, coordinates.getY());
            ps.setInt(3, coordinatesId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCoordinates(int coordinatesId) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement("DELETE FROM coordinates WHERE id = ?");
            ps.setInt(1, coordinatesId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getCoordinatesIdPersonId(long orgId) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();

        int id = -1;
        try (PreparedStatement ps = connection.prepareStatement("select id from coordinates where id = ?")) {
            connection.prepareStatement("select id from coordinates where id = ?");
            ps.setLong(1, orgId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
}
