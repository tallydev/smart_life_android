package com.tallty.smart_life_android.utils;

import java.math.BigDecimal;

/**
 * Created by kang on 2016/12/18.
 * 数值运算工具类
 */

public class ArithUtils {
    // 舍入模式
    public static float round(float value) {
        int scale = 2; //设置位数
        int roundingMode = BigDecimal.ROUND_HALF_DOWN; // 去除位数之后的,不采用四舍五入,对用户不友好
        BigDecimal bd  = BigDecimal.valueOf(value);
        bd = bd.setScale(scale, roundingMode);
        return bd.floatValue();
    }

    // 自定义舍入模式
    public static float customRound(float value, int scale, int mode) {
        BigDecimal bd  = BigDecimal.valueOf(value);
        bd = bd.setScale(scale, mode);
        return bd.floatValue();
    }

    /**
     * 提供精确加法计算的add方法
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1,double value2){
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1,double value2){
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1,double value2){
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     * @param value1 被除数
     * @param value2 除数
     * @param scale 精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(double value1,double value2,int scale) throws IllegalAccessException{
        //如果精确范围小于0，抛出异常信息
        if(scale<0){
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale).doubleValue();
    }
}
