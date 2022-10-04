package com.daniel.server.database;

import com.daniel.common.person.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class LocationDao {

    public static Location getLocationById(int id) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        Location location = new Location();
        try (PreparedStatement ps = connection.prepareStatement("select * from location where id = ?")) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                location.setName(rs.getString("name"));
                location.setX(rs.getInt("x"));
                location.setY(rs.getInt("y"));
                location.setZ(rs.getLong("z"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return location;
    }

    public static int createLocation(Location location) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO location (name, x, y, z) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, location.getName());
            ps.setInt(2, location.getX());
            ps.setInt(3, location.getY());
            ps.setLong(4, location.getZ());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void updateLocation(Location location, int locationId) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();

        try (PreparedStatement ps = connection.prepareStatement("UPDATE location set name = ?, x = ?, y = ?, x = ? WHERE id = ?")) {
            ps.setString(1, location.getName());
            ps.setInt(2, location.getX());
            ps.setInt(3, location.getY());
            ps.setLong(4, location.getZ());
            ps.setInt(5, locationId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeLocation(int locationId) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();

        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM location WHERE id = ?")) {
            ps.setInt(1, locationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLocationIdByPersonId(long personId) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        int id = -1;
        try (PreparedStatement ps = connection.prepareStatement("select location_id from person where id = ?")) {
            ps.setLong(1, personId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt("location_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

}
