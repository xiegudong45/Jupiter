package rpc;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class RpcHelper {

  public static void writeJsonObject(HttpServletResponse response,
                                     JSONObject obj) {
    try {
      response.setContentType("application/json");
      response.addHeader("Access-Control-Allow-Origin", "*");
      PrintWriter out = response.getWriter();
      out.print(obj);
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void writeJSONArray(HttpServletResponse response,
                                    JSONArray arr) {
    try {
      response.setContentType("application/json");
      response.addHeader("Access-Control-Allow-Origin", "*");
      PrintWriter out = response.getWriter();
      out.print(arr);
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static JSONObject readJSONObject(HttpServletRequest request) {
    StringBuilder sb = new StringBuilder();
    try {
      BufferedReader reader = request.getReader();
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      reader.close();
      return new JSONObject(sb.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
