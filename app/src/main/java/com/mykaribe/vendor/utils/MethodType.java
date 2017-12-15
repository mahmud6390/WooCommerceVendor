package com.mykaribe.vendor.utils;

/**
 * Created by USER on 15/12/2017.
 */
public enum MethodType {
    PUT{
        @Override
        public String toString() {
            return "PUT";
        }
    },
    GET{
        @Override
        public String toString() {
            return "GET";
        }
    },
}
