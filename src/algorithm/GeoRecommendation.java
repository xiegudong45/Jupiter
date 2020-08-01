package algorithm;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

import java.util.*;

public class GeoRecommendation {
  public List<Item> recommendItems(String userId, double lat, double lon) {
    List<Item> recommendedItems = new ArrayList<>();
    DBConnection conn = DBConnectionFactory.getConnection();

    // step 1 get all favorite items
    Set<String> favoriteItemIds = conn.getFavoriteItemIds(userId);
    // step 2 get all categories of favorite items, sort by count
    Map<String, Integer> allCategories = new HashMap<>();
    for (String favoriteItemId : favoriteItemIds) {
      Set<String> categories = conn.getCategories(favoriteItemId);
      for (String category : categories) {
        allCategories.put(category,
                allCategories.getOrDefault(category, 0) + 1);
      }
    }

    List<Map.Entry<String, Integer>> categoryList =
            new ArrayList<>(allCategories.entrySet());
    Collections.sort(categoryList, new Comparator<Map.Entry<String, Integer>>() {
      @Override
      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        return Integer.compare(o2.getValue(), o1.getValue());
      }
    });

    // step 3 do search based on categories, filter out favorite events, sort
    // by distance
    Set<Item> visitedItems = new HashSet<>();
    for (Map.Entry<String, Integer> categoryEntry : categoryList) {
      List<Item> items = conn.searchItems(lat, lon, categoryEntry.getKey());
      List<Item> filteredItems = new ArrayList<>();
      for (Item item : items) {
        if (visitedItems.add(item) && !favoriteItemIds.contains(item.getItemId())) {
          filteredItems.add(item);
        }
      }
      Collections.sort(filteredItems, new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
          return Double.compare(o1.getDistance(), o2.getDistance());
        }
      });
      recommendedItems.addAll(filteredItems);
    }


    return recommendedItems;
  }
}
