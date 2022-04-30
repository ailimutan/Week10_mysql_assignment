import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {

  public static void main(String[] args) {

    //setting up connections to MySQL database server
    String jdbcUrl = "jdbc:mysql://localhost:3306/moviemanager";
    String username = "root";
    String password = "password";

    Movie movie = new Movie();
    int choice = 0;

    // Create a Scanner object for keyboard input.
    Scanner scanLogger = new Scanner(System.in);

    do {
      System.out.println("========== MOVIE MENU =============");
      System.out.println(" Input 1 to DISPLAY ALL MOVIES");
      System.out.println(" Input 2 to INSERT NEW MOVIE");
      System.out.println(" Input 3 to UPDATE ALL MOVIE DETAILS");
      System.out.println(" Input 4 to DELETE A MOVIE");
      System.out.println("===================================");

      System.out.print("INPUT CHOICE:");
      choice = scanLogger.nextInt();

      try {
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);

        if (conn != null && choice != 0) {
          System.out.println("Connected to the database..\n");

          if (choice == 1) {
            displayMovies(conn);
          }

          if (choice == 2) {
            movie = instantiateMovie();
            addMovie(conn, movie);
          }

          if (choice == 3) {
            displayMovies(conn);
            System.out.println("Please enter movie id that you like to UPDATE:");
            Scanner logger = new Scanner(System.in);
            int movie_id = logger.nextInt();

            System.out.println("----- INPUT NEW VALUE FOR EACH MOVIE DETAILS -----");
            movie = instantiateMovie();
            System.out.println("You choose to UPDATE movie with the id -> " + movie_id);
            updateMovie(conn, movie, movie_id);

          }

          if (choice == 4) {
            displayMovies(conn);
            System.out.print("Please enter movie id that you like to DELETE:");
            Scanner logger = new Scanner(System.in);
            int movie_id = logger.nextInt();

            System.out.println("You choose to DELETE movie with the id -> " + movie_id);
            deleteMovie(conn, movie_id);

          }

          if (choice > 4) {
            System.out.println("Please enter a valid choice...");
          }

          conn.close();
        }

      } catch (SQLException ex) {
        ex.printStackTrace();
      }
      if (choice == 0) {
        System.out.println("You have exited from the app...");
      }

    } while (choice != 0);

  }

  /*
   * This method get the necessary values of movie information from the user
   */
  public static Movie instantiateMovie() {

    Scanner logger = new Scanner(System.in);

    Movie newMovie = new Movie();
    System.out.println("Enter Movie Name:");
    newMovie.name = logger.nextLine();

    System.out.println("Enter Movie Genre:");
    newMovie.genre = logger.nextLine();

    System.out.println("Enter Movie Country:");
    newMovie.country_origin = logger.nextLine();

    do {
      System.out.println("Enter Movie Year:");
      newMovie.year_released = logger.nextInt();
    } while (newMovie.year_released <= 1000);

    return newMovie;
  }

  /*
   * This method performs SELECT operation in the database
   */

  public static void displayMovies(Connection conn) {
    String sql = "SELECT * FROM Movies";

    try {
      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery(sql);

      System.out.println("====================== RESULT ======================");
      while (result.next()) {
        int movie_id = result.getInt("movie_id");
        String name = result.getString("name");
        String genre = result.getString("genre");
        int year = result.getInt("year_released");
        String country = result.getString("country_origin");

        System.out.println("Movie #" + movie_id + ": " + name + ", " + genre + ", " + year + ", " + country);
      }

      System.out.println("====================================================\n");

    } catch (SQLException ex) {
      ex.printStackTrace();
    }

  }

  /*
   * This method performs INSERT operation in the database
   */
  public static void addMovie(Connection conn, Movie movie) {
    String sql = "INSERT INTO Movies (name, genre, year_released, country_origin)" +
      " VALUES (?, ?, ?, ?)";

    try {
      PreparedStatement statement = conn.prepareStatement(sql);

      statement.setString(1, movie.name);
      statement.setString(2, movie.genre);
      statement.setInt(3, movie.year_released);
      statement.setString(4, movie.country_origin);

      int rows = statement.executeUpdate();

      if (rows > 0) {
        System.out.println("======================RESULT===========================");
        System.out.println("||                                                   ||");
        System.out.println("|| Success! New movie has been added in the database.||");
        System.out.println("||                                                   ||");
        System.out.println("=======================================================\n");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

  }

  /*
   * This method performs UPDATE operation in the database
   */
  public static void updateMovie(Connection conn, Movie movie, int id) {

    String sql = "UPDATE Movies SET name=?, genre=?, year_released=?, country_origin=? WHERE movie_id=?";

    try {
      PreparedStatement statement = conn.prepareStatement(sql);

      statement.setString(1, movie.name);
      statement.setString(2, movie.genre);
      statement.setInt(3, movie.year_released);
      statement.setString(4, movie.country_origin);
      statement.setInt(5, id);

      int rows = statement.executeUpdate();

      if (rows > 0) {
        System.out.println("======================RESULT===========================");
        System.out.println("||                                                   ||");
        System.out.println("|| Success! The movie's information has been updated.||");
        System.out.println("||                                                   ||");
        System.out.println("=======================================================\n");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

  }

  /*
   * This method performs DELETE operation in the database
   */
  public static void deleteMovie(Connection conn, int id) {

    String sql = "DELETE FROM Movies WHERE movie_id=?";

    try {
      PreparedStatement statement = conn.prepareStatement(sql);

      statement.setInt(1, id);

      int rows = statement.executeUpdate();

      if (rows > 0) {
        System.out.println("======================RESULT===========================");
        System.out.println("||                                                   ||");
        System.out.println("|| Success! The movie's information has been deleted.||");
        System.out.println("||                                                   ||");
        System.out.println("=======================================================\n");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

  }

}