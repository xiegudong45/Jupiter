package rpc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterAPI;
import org.json.JSONArray;
import org.json.JSONException;
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
    JSONArray arr = new JSONArray();
    try {
      double lat = Double.parseDouble(request.getParameter("lat"));
      double lon = Double.parseDouble(request.getParameter("lon"));
      String keyword = request.getParameter("term");

      DBConnection conn = DBConnectionFactory.getConnection();
      List<Item> items = conn.searchItems(lat, lon, keyword);

      for (Item item : items) {
        JSONObject obj = item.toJSONObject();
        arr.put(obj);
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
