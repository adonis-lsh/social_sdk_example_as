//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.ShareBoard;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ShareAction {
    private ShareContent mShareContent = new ShareContent();
    private SHARE_MEDIA mPlatform = null;
    private UMShareListener mListener = null;
    private ShareBoardlistener boardlistener = null;
    private Activity activity;
    private List<SHARE_MEDIA> displaylist = null;
    private List<SnsPlatform> platformlist = new ArrayList();
    private List<ShareContent> contentlist = new ArrayList();
    private List<UMShareListener> listenerlist = new ArrayList();
    private int gravity = 80;
    private View showatView = null;
    private ShareBoard mShareBoard;
    private ShareBoardlistener defaultshareboardlistener = new ShareBoardlistener() {
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            ShareAction.this.setPlatform(share_media);
            ShareAction.this.share();
        }
    };
    private ShareBoardlistener defaultmulshareboardlistener = new ShareBoardlistener() {
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            int index = ShareAction.this.displaylist.indexOf(share_media);
            int contentsize = ShareAction.this.contentlist.size();
            if (contentsize != 0) {
                ShareContent s;
                if (index < contentsize) {
                    s = (ShareContent) ShareAction.this.contentlist.get(index);
                } else {
                    s = (ShareContent) ShareAction.this.contentlist.get(contentsize - 1);
                }

                ShareAction.this.mShareContent = s;
            }

            int listenersize = ShareAction.this.listenerlist.size();
            if (listenersize != 0) {
                if (index < listenersize) {
                    ShareAction.this.mListener = (UMShareListener) ShareAction.this.listenerlist.get(index);
                } else {
                    ShareAction.this.mListener = (UMShareListener) ShareAction.this.listenerlist.get(listenersize - 1);
                }
            }

            ShareAction.this.setPlatform(share_media);
            ShareAction.this.share();
        }
    };

    public ShareAction(Activity activity) {
        if (activity != null) {
            this.activity = (Activity) (new WeakReference(activity)).get();
        }

    }

    public ShareContent getShareContent() {
        return this.mShareContent;
    }

    public boolean getUrlValid() {
        return this.mShareContent == null || this.mShareContent.mMedia == null || !(this.mShareContent.mMedia instanceof UMWeb) || this.mShareContent.mMedia.toUrl().startsWith("http");
    }

    public SHARE_MEDIA getPlatform() {
        return this.mPlatform;
    }

    public ShareAction setPlatform(SHARE_MEDIA platform) {
        this.mPlatform = platform;
        return this;
    }

    public ShareAction setCallback(UMShareListener listener) {
        this.mListener = listener;
        return this;
    }

    public ShareAction setShareboardclickCallback(ShareBoardlistener listener) {
        this.boardlistener = listener;
        return this;
    }

    public ShareAction setShareContent(ShareContent shareContent) {
        this.mShareContent = shareContent;
        return this;
    }

    public ShareAction setDisplayList(SHARE_MEDIA... list) {
        this.displaylist = Arrays.asList(list);
        this.platformlist.clear();
        Iterator var2 = this.displaylist.iterator();

        while (var2.hasNext()) {
            SHARE_MEDIA temp = (SHARE_MEDIA) var2.next();
            this.platformlist.add(temp.toSnsPlatform());
        }

        return this;
    }


    @Deprecated
    public ShareAction setListenerList(UMShareListener... list) {
        this.listenerlist = Arrays.asList(list);
        return this;
    }


    @Deprecated
    public ShareAction setContentList(ShareContent... list) {
        if (list != null && Arrays.asList(list).size() != 0) {
            this.contentlist = Arrays.asList(list);
        } else {
            ShareContent content = new ShareContent();
            content.mText = "empty";
            this.contentlist.add(content);
        }

        return this;
    }

    public ShareAction addButton(String showword, String Keyword, String icon, String Grayicon) {
        this.platformlist.add(SHARE_MEDIA.createSnsPlatform(showword, Keyword, icon, Grayicon, 0));
        return this;
    }

    public ShareAction withText(String text) {
        this.mShareContent.mText = text;
        return this;
    }

    public ShareAction withSubject(String subject) {
        this.mShareContent.subject = subject;
        return this;
    }

    public ShareAction withFile(File file) {
        this.mShareContent.file = file;
        return this;
    }

    public ShareAction withApp(File file) {
        this.mShareContent.app = file;
        return this;
    }

    public ShareAction withMedia(UMImage image) {
        this.mShareContent.mMedia = image;
        return this;
    }

    public ShareAction withMedia(UMMin umMin) {
        this.mShareContent.mMedia = umMin;
        return this;
    }

    public ShareAction withMedia(UMEmoji image) {
        this.mShareContent.mMedia = image;
        return this;
    }

    public ShareAction withMedia(UMWeb web) {
        this.mShareContent.mMedia = web;
        return this;
    }

    public ShareAction withFollow(String follow) {
        this.mShareContent.mFollow = follow;
        return this;
    }

    public ShareAction withExtra(UMImage mExtra) {
        this.mShareContent.mExtra = mExtra;
        return this;
    }

    public ShareAction withMedia(UMusic music) {
        this.mShareContent.mMedia = music;
        return this;
    }

    public ShareAction withMedia(UMVideo video) {
        this.mShareContent.mMedia = video;
        return this;
    }

    public ShareAction withShareBoardDirection(View view, int gravity) {
        this.gravity = gravity;
        this.showatView = view;
        return this;
    }

    public void share() {
        UMShareAPI.get(this.activity).doShare(this.activity, this, this.mListener);
    }

    public void open(ShareBoardConfig config) {
        HashMap map;
        if (this.platformlist.size() != 0) {
            map = new HashMap();
            map.put("listener", this.mListener);
            map.put("content", this.mShareContent);

            try {
                this.mShareBoard = new ShareBoard(this.activity, this.platformlist, config);
                if (this.boardlistener == null) {
                    this.mShareBoard.setShareBoardlistener(this.defaultmulshareboardlistener);
                } else {
                    this.mShareBoard.setShareBoardlistener(this.boardlistener);
                }

                this.mShareBoard.setFocusable(true);
                this.mShareBoard.setBackgroundDrawable(new BitmapDrawable());
                if (this.showatView == null) {
                    this.showatView = this.activity.getWindow().getDecorView();
                }

                this.mShareBoard.showAtLocation(this.showatView, this.gravity, 0, 0);
            } catch (Exception var4) {
                Log.e("");
            }
        } else {
            this.platformlist.add(SHARE_MEDIA.WEIXIN.toSnsPlatform());
            this.platformlist.add(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform());
            this.platformlist.add(SHARE_MEDIA.SINA.toSnsPlatform());
            this.platformlist.add(SHARE_MEDIA.QQ.toSnsPlatform());
            map = new HashMap();
            map.put("listener", this.mListener);
            map.put("content", this.mShareContent);
            this.mShareBoard = new ShareBoard(this.activity, this.platformlist, config);
            if (this.boardlistener == null) {
                this.mShareBoard.setShareBoardlistener(this.defaultshareboardlistener);
            } else {
                this.mShareBoard.setShareBoardlistener(this.boardlistener);
            }

            this.mShareBoard.setFocusable(true);
            this.mShareBoard.setBackgroundDrawable(new BitmapDrawable());
            if (this.showatView == null) {
                this.showatView = this.activity.getWindow().getDecorView();
            }

            this.mShareBoard.showAtLocation(this.showatView, 80, 0, 0);
        }

    }

    public void open() {
        this.open((ShareBoardConfig) null);
    }

    public void close() {
        if (this.mShareBoard != null) {
            this.mShareBoard.dismiss();
            this.mShareBoard = null;
        }

    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) {
            return null;
        } else {
            try {
                v.getLocationOnScreen(loc_int);
            } catch (NullPointerException var3) {
                return null;
            }

            Rect location = new Rect();
            location.left = loc_int[0];
            location.top = loc_int[1];
            location.right = location.left + v.getWidth();
            location.bottom = location.top + v.getHeight();
            return location;
        }
    }
}
