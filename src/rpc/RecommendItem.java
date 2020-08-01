package rpc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/recommendation")
public class RecommendItem extends HttpServlet {
  private static final long serialVersionID = 1L;

  public RecommendItem() {
    super();
  }

  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
          IOException {

    JSONArray arr = new JSONArray();
    String[] names = {"abcd", "1234"};
    String[] addrs = {"san francisco", "san jose"};
    String[] times = {"01/01/2017", "01/02/2017"};

    try {
      for (int i = 0; i < 2; i++) {
        JSONObject obj = new JSONObject();
        obj.put("name", names[i]);
        obj.put("addr", addrs[i]);
        obj.put("time", times[i]);
        arr.put(obj);
      }
    } catch ( JSONException e) {
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
