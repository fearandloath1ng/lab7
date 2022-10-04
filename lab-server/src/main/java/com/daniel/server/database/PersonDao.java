package com.daniel.server.database;

import com.daniel.common.person.Person;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.daniel.server.database.CoordinatesDao.createCoordinates;
import static com.daniel.server.database.CoordinatesDao.getCoordinatesById;
import static com.daniel.server.database.CoordinatesDao.getCoordinatesIdPersonId;
import static com.daniel.server.database.CoordinatesDao.removeCoordinates;
import static com.daniel.server.database.CoordinatesDao.updateCoordinates;
import static com.daniel.server.database.HairColorDao.getHairColorById;
import static com.daniel.server.database.HairColorDao.getHairColorIdByName;
import static com.daniel.server.database.LocationDao.createLocation;
import static com.daniel.server.database.LocationDao.getLocationById;
import static com.daniel.server.database.LocationDao.getLocationIdByPersonId;
import static com.daniel.server.database.LocationDao.removeLocation;
import static com.daniel.server.database.LocationDao.updateLocation;
import static com.daniel.server.database.UserDao.getUserById;

public final class PersonDao {

    private PersonDao() {
    }
    public static List<Person> getAllPersonFromDB() {
        List<Person> people = new ArrayList<>();
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from person")) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getInt("id"));
                person.setName(rs.getString("name"));
                person.setCoordinates(getCoordinatesById(rs.getInt("coordinates_id")));
                person.setCreationDate(rs.getTimestamp("creationDate"));
                person.setHeight(rs.getInt("height"));
                person.setBirthday(rs.getTimestamp("birthDay").toLocalDateTime());
                person.setWeight(rs.getFloat("location_id"));
                person.setHairColor(getHairColorById(rs.getInt("hair_color_id")));
                person.setLocation(getLocationById(rs.getInt("location_id")));
                person.setUser(getUserById(rs.getInt("user_id")));
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public static Person getPersonById(long id) {
        Person person = null;
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from person where id = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                person.setId(rs.getInt("id"));
                person.setName(rs.getString("name"));
                person.setCoordinates(getCoordinatesById(rs.getInt("coordinates_id")));
                person.setCreationDate(rs.getTimestamp("creationDate"));
                person.setHeight(rs.getInt("height"));
                person.setBirthday(rs.getTimestamp("birthDay").toLocalDateTime());
                person.setWeight(rs.getFloat("chapter_id"));
                person.setHairColor(getHairColorById(rs.getInt("hair_color_id")));
                person.setLocation(getLocationById(rs.getInt("location_id")));
                person.setUser(getUserById(rs.getInt("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public static void createPerson(Person person, int userId) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO person "
                + "(name, coordinates_id, \"creationDate\", height, \"birthDay\", weight," +
                " hair_color_id, location_id, user_id) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            int index = 1;
            ps.setString(index++, person.getName());
            ps.setLong(index++, createCoordinates(person.getCoordinates()));
            ps.setDate(index++, new Date(System.currentTimeMillis()));
            ps.setInt(index++, person.getHeight());
            ps.setDate(index++, new Date(Date.from(person.getBirthday().toInstant(ZoneOffset.UTC)).getTime()));
            ps.setFloat(index++, person.getWeight());
            ps.setInt(index++, getHairColorIdByName(person.getHairColor().name()));
            ps.setInt(index++, createLocation(person.getLocation()));
            ps.setInt(index, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Person person, long personId) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("UPDATE person set name = ?,"
                + "height = ?, \"birthDay\" = ?, weight = ?, hair_color_id = ?"
                + "WHERE id = ?")) {
            int index = 1;
            ps.setString(index++, person.getName());
            ps.setInt(index++, person.getHeight());
            ps.setDate(index++, new Date(Date.from(person.getBirthday().toInstant(ZoneOffset.UTC)).getTime()));
            ps.setFloat(index++, person.getWeight());
            ps.setInt(index++, getHairColorIdByName(person.getHairColor().name()));
            ps.setLong(index, personId);

            updateCoordinates(person.getCoordinates(), getCoordinatesIdPersonId(person.getId()));
            updateLocation(person.getLocation(), getLocationIdByPersonId(person.getId()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeById(long id) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM person WHERE id = ?")) {
            ps.setLong(1, id);

            removeCoordinates(getCoordinatesIdPersonId(id));
            removeLocation(getLocationIdByPersonId(id));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
