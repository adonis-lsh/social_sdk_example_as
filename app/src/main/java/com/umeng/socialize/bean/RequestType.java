//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.bean;

public enum RequestType {
    SOCIAL {
        public String toString() {
            return "0";
        }
    },
    @Deprecated
    ANALYTICS {
        public String toString() {
            return "1";
        }
    },
    @Deprecated
    API {
        public String toString() {
            return "2";
        }
    };

    private RequestType() {
    }
}
