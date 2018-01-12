//
// Source code recreated from BitmapUtil .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.socialize.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.umeng.socialize.utils.SocializeUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueuedWork {
    public static boolean isUseThreadPool = false;
    private static Handler uiHandler;
    private static ExecutorService mLogicExecutor = Executors.newFixedThreadPool(5);
    private static ExecutorService mNetExecutor = Executors.newFixedThreadPool(5);

    public QueuedWork() {
    }

    public static void runInMain(Runnable var0) {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }

        uiHandler.post(var0);
    }

    public static void runInBack(Runnable var0, boolean var1) {
        if (isUseThreadPool) {
            if (var1) {
                mNetExecutor.execute(var0);
            } else {
                mLogicExecutor.execute(var0);
            }
        } else {
            (new Thread(var0)).start();
        }

    }

    public abstract static class UMAsyncTask<Result> {
        protected Runnable thread;

        public UMAsyncTask() {
        }

        protected void onPreExecute() {
        }

        protected abstract Result doInBackground();

        protected void onPostExecute(Result result) {
        }

        public final QueuedWork.UMAsyncTask<Result> execute() {
            this.thread = new Runnable() {
                public void run() {
                    final Result result = UMAsyncTask.this.doInBackground();
                    QueuedWork.runInMain(new Runnable() {
                        public void run() {
                            UMAsyncTask.this.onPostExecute(result);
                        }
                    });
                }
            };
            QueuedWork.runInMain(new Runnable() {
                public void run() {
                    UMAsyncTask.this.onPreExecute();
                }
            });
            QueuedWork.runInBack(this.thread, false);
            return this;
        }
    }

    public abstract static class DialogThread<T> extends QueuedWork.UMAsyncTask {
        Dialog dialog = null;

        public DialogThread(Context var1) {
        }

        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            SocializeUtils.safeCloseDialog(this.dialog);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            SocializeUtils.safeShowDialog(this.dialog);
        }
    }
}
