package rpc;

import algorithm.GeoRecommendation;
import entity.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/recommendation")
public class RecommendItem extends HttpServlet {
  private static final long serialVersionID = 1L;

  public RecommendItem() {
    super();
  }

  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
          IOException {

    String userId = request.getParameter("user_id");
    double lat = Double.parseDouble(request.getParameter("lat"));
    double lon = Double.parseDouble(request.getParameter("lon"));
    // double lat = 37.38;
    // double lon = -122.08;
    GeoRecommendation geoRecommendation = new GeoRecommendation();
    List<Item> items = geoRecommendation.recommendItems(userId, lat, lon);

    JSONArray arr = new JSONArray();
    try {
      for (Item item : items) {
        arr.put(item.toJSONObject());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    RpcHelper.writeJSONArray(response, arr);
    // response.getWriter().append("Served at: ").append(request.getContextPath());
  }

  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response) throws ServletException,
          IOException {
    doGet(request, response);
  }
}
