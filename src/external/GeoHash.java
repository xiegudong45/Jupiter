package external;

public class GeoHash {
  private static final String BASE_32 = "0123456789bcdefghjkmnpqrstuvwxyz";
  private static int divideRangeByValue(double value, double[] range) {
    double mid = middle(range);
    if (value >= mid) {
      range[0] = mid;
      return 1;
    } else {
      range[1] = mid;
      return 0;
    }
  }

  private static double middle(double[] range) {
    return (range[0] + range[1]) / 2;
  }

  public static String encodeGeoHash(double latitude, double longitude,
                                     int precision) {
    double[] latRange = new double[]{-90.0, 90.0};
    double[] lonRange = new double[]{-180.0, 180.0};
    boolean isEven = true;
    int bit = 0;
    int base32CharIndex = 0;
    StringBuilder geoHash = new StringBuilder();
    while (geoHash.length() < precision) {
      if (isEven) {
        base32CharIndex =
                (base32CharIndex << 1) | divideRangeByValue(longitude,
                        lonRange);
      } else {
        base32CharIndex =
                (base32CharIndex << 1) | divideRangeByValue(latitude, latRange);
      }
      isEven = !isEven;
      if (bit < 4) {
        bit++;
      } else {
        geoHash.append(BASE_32.charAt(base32CharIndex));
        bit = 0;
        base32CharIndex = 0;
      }
    }
    return geoHash.toString();
  }

  public static void main(String[] args) {
    System.out.println(encodeGeoHash(57.64911, 10.40744, 8));
  }
}
