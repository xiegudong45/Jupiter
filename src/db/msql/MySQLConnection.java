package db.msql;

import db.DBConnection;
import entity.Item;
import external.TicketMasterAPI;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySQLConnection implements DBConnection {
  private Connection conn;

  public MySQLConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
      conn = DriverManager.getConnection(MySQLDBUtil.URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {
    if (conn != null) {
      try {
        conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setFavoriteItems(String userId, List<String> itemIds) {
    if (conn != null) {
      try {
        String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (String itemId : itemIds) {
          stmt.setString(1, userId);
          stmt.setString(2, itemId);
          stmt.execute();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("conn is null");
    }
  }

  @Override
  public void unsetFavoriteItems(String userId, List<String> itemIds) {
    if (conn != null) {
      try {
        String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
        PreparedStatement smt = conn.prepareStatement(sql);
        for (String itemId : itemIds) {
          smt.setString(1, userId);
          smt.setString(2, itemId);
          smt.execute();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Set<String> getFavoriteItemIds(String userId) {
    if (conn == null) {
      return new HashSet<>();
    }
    Set<String> favoriteItemIds = new HashSet<>();
    try {
      String sql = "SELECT item_id FROM history WHERE user_id = ?";
      PreparedStatement smt = conn.prepareStatement(sql);

      smt.setString(1, userId);
      ResultSet rs = smt.executeQuery();

      while (rs.next()) {
        favoriteItemIds.add(rs.getString("item_id"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return favoriteItemIds;
  }

  @Override
  public Set<Item> getFavoriteItems(String userId) {
    if (conn == null) {
      return new HashSet<>();
    }

    Set<Item> favoriteItems = new HashSet<>();
    Set<String> itemIds = getFavoriteItemIds(userId);
    try {
      String sql = "SELECT * FROM items WHERE item_id = ?";
      PreparedStatement smt = conn.prepareStatement(sql);
      for (String itemId : itemIds) {
        smt.setString(1, itemId);
        ResultSet rs = smt.executeQuery();
        Item.ItemBuilder builder = new Item.ItemBuilder();
        while (rs.next()) {
          builder.setItemId(rs.getString("item_id"));
          builder.setName(rs.getString("name"));
          builder.setAddress(rs.getString("address"));
          builder.setImageUrl(rs.getString("image_url"));
          builder.setUrl(rs.getString("url"));
          builder.setCategories(getCategories(itemId));
          builder.setDistance(rs.getDouble("distance"));
          builder.setRating(rs.getDouble("rating"));

          favoriteItems.add(builder.build());
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return favoriteItems;
  }

  @Override
  public Set<String> getCategories(String itemId) {
    if (conn == null) {
      return new HashSet<>();
    }
    Set<String> categories = new HashSet<>();
    try {
      String sql = "SELECT category FROM categories WHERE item_id = ?";
      PreparedStatement smt = conn.prepareStatement(sql);
      smt.setString(1, itemId);
      ResultSet rs = smt.executeQuery();
      while (rs.next()) {
        categories.add(rs.getString("category"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return categories;
  }

  @Override
  public List<Item> searchItems(double lat, double lon, String term) {
    TicketMasterAPI ticketMasterAPI = new TicketMasterAPI();
    List<Item> items = ticketMasterAPI.search(lat, lon, term);
    for (Item item : items) {
      saveItem(item);
    }
    return items;
  }

  @Override
  public void saveItem(Item item) {
    if (conn != null) {
      try {
        String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setString(1, item.getItemId());
        smt.setString(2, item.getName());
        smt.setDouble(3, item.getRating());
        smt.setString(4, item.getAddress());
        smt.setString(5, item.getImageUrl());
        smt.setString(6, item.getUrl());
        smt.setDouble(7, item.getDistance());
        smt.execute();

        sql = "INSERT IGNORE INTO categories VALUES (?, ?)";
        smt = conn.prepareStatement(sql);
        for (String category : item.getCategories()) {
          smt.setString(1, item.getItemId());
          smt.setString(2, category);
          smt.execute();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String getFullName(String userId) {
    if (conn == null) {
      return null;
    }
    String name = "";
    try {
      String sql = "SELECT first_name, last_name FROM users WHERE user_id = ?";
      PreparedStatement smt = conn.prepareStatement(sql);
      smt.setString(1, userId);
      ResultSet rs = smt.executeQuery();
      if (rs.next()) {
        name = String.join(" ", rs.getString("first_name"), rs.getString(
                "last_name"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return name;
  }

  @Override
  public boolean verifyLogin(String userId, String password) {
    if (conn == null) {
      return false;
    }
    try {
      String sql = "SELECT password FROM users WHERE user_id = ? AND password" +
              " = ?";
      PreparedStatement smt = conn.prepareStatement(sql);
      smt.setString(1, userId);
      smt.setString(2, password);
      ResultSet rs = smt.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
