package kz.pompei.logisizix.app;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;

public class HelloWorld {

  @SneakyThrows
  public static void main(String[] args) {

    final Path home = Paths.get(System.getProperty("user.home"));

    final Path appDir = home.resolve(".local").resolve("logisizix");

    final File databaseFile = appDir.resolve("database").toFile();

    databaseFile.getParentFile().mkdirs();

    String url = "jdbc:h2:file:" + databaseFile.getAbsolutePath();
    Class.forName("org.h2.Driver");

    try (Connection connection = DriverManager.getConnection(url)) {

      System.out.println("atvLd79LZb :: " + connection);

      Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

      final Liquibase liquibase = new Liquibase("liquibase/main.xml", new ClassLoaderResourceAccessor(), database);

      liquibase.update();

    }

  }

}
