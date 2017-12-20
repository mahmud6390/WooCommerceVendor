package com.mykaribe.vendor.utils;

/**
 * Created by USER on 20/12/2017.
 */
public enum OrderStatus {
    PROCESSING{
        @Override
        public String toString() {
            return "processing";
        }
    },
    ON_HOLD{
        @Override
        public String toString() {
            return "on-hold";
        }
    },
    COMPLETED{
        @Override
        public String toString() {
            return "completed";
        }
    },
}
