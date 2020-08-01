package db;

import db.msql.MySQLConnection;
import entity.Item;

import java.util.List;
import java.util.Set;

public class DBConnectionFactory implements DBConnection {
  private static final String DEFAULT_DB = "mysql";

  public static DBConnection getConnection(String db) {
    switch (db) {
      case "mysql":
        return new MySQLConnection();
      case "mongodb":
        return null;
        // return new MongoDBConnection();
      default:
        throw new IllegalArgumentException("Invalid input");
    }
  }

  public static DBConnection getConnection() {
    return getConnection(DEFAULT_DB);
  }

  @Override
  public void close() {

  }

  @Override
  public void setFavoriteItems(String userId, List<String> itemIds) {

  }

  @Override
  public void unsetFavoriteItems(String userId, List<String> itemIds) {

  }

  @Override
  public Set<String> getFavoriteItemIds(String userId) {
    return null;
  }

  @Override
  public Set<Item> getFavoriteItems(String userId) {
    return null;
  }

  @Override
  public Set<String> getCategories(String itemId) {
    return null;
  }

  @Override
  public List<Item> searchItems(double lat, double lon, String term) {
    return null;
  }

  @Override
  public void saveItem(Item item) {

  }

  @Override
  public String getFullName(String userId) {
    return null;
  }

  @Override
  public boolean verifyLogin(String userId, String password) {
    return false;
  }
}
