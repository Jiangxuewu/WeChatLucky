package com.bb_sz.gobang;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/11.
 */

public class Test {

    final static int[] nums = {47, 0, 62, 4, 34, 46, 36, 55, 48, 66, 2, 81, 90, 94, 58, 13, 93, 42, 40, 3, 31, 72, 54, 63, 21, 67, 6, 5, 24, 86, 10, 45, 26, 1, 19, 15, 83, 74, 71, 64, 82, 14, 57, 52, 28, 61, 37, 51, 92, 35, 69, 18, 50, 89, 65, 56, 53, 84, 33, 9, 97, 60, 32, 25, 70, 85, 43, 23, 73};
    String tmp = "00, 01, 02, 03, 04, 05, 06, 09, 10, 13, 14, 15, 18, 19, 21, 23, 24, 25, 26, 28, 31, 32, 33, 34, 35, 36, 37, 40, 42, 43, 45, 46, 47, 48, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 61, 62, 63, 64, 65, 66, 67, 69, 70, 71, 72, 73, 74, 81, 82, 83, 84, 85, 86, 89, 90, 92, 93, 94, 97";

    public static void main(String[] args) {
        System.out.print("start:" + System.currentTimeMillis() + " \n");
        Arrays.sort(nums);
        System.out.print("end:" + System.currentTimeMillis() + " \n");
        for (int i = 0; i < nums.length; i++) {
            if (i != 0) {
                System.out.print(", " + nums[i]);
            } else {
                System.out.print(nums[i]);
            }
        }

        if (1 == 1) return;
        int count = 0;

        //统计所有可能的赢法,需要好好理解
        //共 572 赢法
        boolean[][][] wins = new boolean[15][15][572];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                for (int k = 0; k < 5; k++) {
                    wins[i][j + k][count] = true;
                }
                count++;
            }
        }
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 15; j++) {
                for (int k = 0; k < 5; k++) {
                    wins[i + k][j][count] = true;
                }
                count++;
            }
        }
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                for (int k = 0; k < 5; k++) {
                    wins[i + k][j + k][count] = true;
                }
                count++;
            }
        }
        for (int i = 0; i < 11; i++) {
            for (int j = 14; j > 3; j--) {
                for (int k = 0; k < 5; k++) {
                    wins[i + k][j - k][count] = true;
                }
                count++;
            }
        }
        for (int i = 0; i < 572; i++) {
            for (int j = 0; j < 15; j++) {
                for (int k = 0; k < 15; k++) {
                    if (wins[k][j][i]) {
                        System.out.print(" " + (i + 1) + " " + (j + 1) + " " + (k + 1) + "\n");
                    }
                }
            }
        }
    }
}
