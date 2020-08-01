package rpc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/search")
public class SearchItem extends HttpServlet {
  private static final long serialVersionID = 1L;

  public SearchItem() {
    super();
  }

  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
          IOException {
    // System.out.println(request);
    String userId = request.getParameter("user_id");
    double lat = Double.parseDouble(request.getParameter("lat"));
    double lon = Double.parseDouble(request.getParameter("lon"));
    // double lat = 37.38;
    // double lon = -122.08;
    String keyword = request.getParameter("term");
    System.out.println("lat: " + lat);
    System.out.println("long: " + lon);
    DBConnection conn = DBConnectionFactory.getConnection();
    List<Item> items = conn.searchItems(lat, lon, keyword);

    Set<String> favorite = conn.getFavoriteItemIds(userId);

    List<JSONObject> list = new ArrayList<>();
    try {
      for (Item item : items) {
        // Add a thin version of restaurant object
        JSONObject obj = item.toJSONObject();
        // Check if this is a favorite one.
        // This field is required by frontend to correctly display favorite items.
        obj.put("favorite", favorite.contains(item.getItemId()));

        list.add(obj);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    JSONArray arr = new JSONArray(list);
    RpcHelper.writeJSONArray(response, arr);
    // response.getWriter().append("Served at: ").append(request.getContextPath());
  }

  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response) throws ServletException,
          IOException {
    doGet(request, response);
  }

}
