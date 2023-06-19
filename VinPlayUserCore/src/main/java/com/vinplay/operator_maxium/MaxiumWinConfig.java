package com.vinplay.operator_maxium;

public class MaxiumWinConfig {  // app dung cho cac game choi voi nha cai

    public static int[] listMinestone = {100000,500000,1000000,2000000,5000000,2147483640};
    public static int[] listMulti = {2147483640,2147483640,2147483640,2147483640,2147483640,2147483640,2147483640};         // chia 10

    public static int getIndexUser(long userValue){
        for(int i =0;i<listMinestone.length;i++){
            if (userValue< listMinestone[i]){
                return i;
            }
        }
        return  listMinestone.length;
    }

    public static long getMaxiumMoneyWin(long userValue){
        int index = getIndexUser(userValue);
        if(index == 0){
            return listMulti[0];
        }
        return userValue*listMulti[index]/10;
    }
}
