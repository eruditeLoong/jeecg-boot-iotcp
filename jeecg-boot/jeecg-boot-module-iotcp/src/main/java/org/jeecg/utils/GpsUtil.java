package org.jeecg.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GpsUtil {

    public static double pi = 3.1415926535897932384626;
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

    public static double[] gps84_To_Gcj02(double lon, double lat) {
        if (outOfChina(lat, lon)) {
            return new double[]{lon, lat};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLon, mgLat};
    }

    //求两坐标距离
    public static double distance(double lngA, double latA, double lngB, double latB) {
        int earthR = 6371000;
        double x = Math.cos(latA * pi / 180.) * Math.cos(latB * pi / 180.) * Math.cos((lngA - lngB) * pi / 180);
        double y = Math.sin(latA * pi / 180.) * Math.sin(latB * pi / 180.);
        double s = x + y;
        if (s > 1) s = 1;
        if (s < -1) s = -1;
        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }

    public static void main(String[] args) {

        File file = new File("/Users/zhouwenrong/Project/项目/物联网/人员定位/gps.txt");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));//构造一个BufferedReader类来读取文件

            String s = null;
            List<double[]> list = new ArrayList<>();
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                if(s.indexOf("$GNGGA") != -1){
                    String y = s.split(",")[2];
                    String x = s.split(",")[4];

                    Double lon = Double.parseDouble(x)/100f;
                    Double lat = Double.parseDouble(y)/100f;

                    double[] b = gps84_To_Gcj02(lon, lat);
                    list.add(b);
                    System.out.println(b[0]+","+b[1]);
                }
            }
            br.close();
            list.toArray();
            for(int i=0;i<list.size()-1;i++) {
                for(int j=0;j<list.size()-1-i;j++){

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
