//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.media;

import java.util.Map;

public interface UMediaObject {
    String toUrl();

    UMediaObject.MediaType getMediaType();

    boolean isUrlMedia();

    Map<String, Object> toUrlExtraParams();

    byte[] toByte();

    public static enum MediaType {
        IMAGE {
            public String toString() {
                return "0";
            }
        },
        VEDIO {
            public String toString() {
                return "1";
            }
        },
        MUSIC {
            public String toString() {
                return "2";
            }
        },
        TEXT {
            public String toString() {
                return "3";
            }
        },
        TEXT_IMAGE {
            public String toString() {
                return "4";
            }
        },
        WEBPAGE {
            public String toString() {
                return "5";
            }
        };

        private MediaType() {
        }
    }
}
