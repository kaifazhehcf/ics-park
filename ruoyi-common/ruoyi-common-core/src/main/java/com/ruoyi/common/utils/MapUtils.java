package com.ruoyi.common.utils;


/**
 * 经纬度处理工具类
 *
 * @author ruoyi
 */
public class MapUtils {

    // private static double EARTH_RADIUS = 6378.137;
    private static double EARTH_RADIUS = 6371.393; //地球半径,单位千米

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个经纬度之间的距离
     *
     * @param lat1(纬度)
     * @param lng1(经度)
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.getDistance(22.796910, 113.303210, 22.796690, 113.299369));
        System.out.println(MapUtils.getDistance(22.800220, 113.288180, 22.796690, 113.299369));
    }
}
