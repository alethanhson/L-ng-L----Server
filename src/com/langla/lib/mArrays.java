/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.lib;

/**
 * @author PKoolVN
 **/
public class mArrays {

    public static String toString(Object[] a) {
        if (a == null) {
            return "null";
        }

        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        for (int i = 0;; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax) {
                return b.append("\n").toString();
            }
            b.append("\n");
        }
    }
}
