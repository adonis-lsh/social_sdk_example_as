package com.umeng.socialize.net.base;

import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.utils.UClient;
import com.umeng.socialize.net.utils.URequest;


public class SocializeClient extends UClient {
    public SocializeReseponse execute(URequest paramURequest) {
        if (SocializeConstants.DEBUG_MODE) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException localInterruptedException) {
            }
        }

        return (SocializeReseponse) super.execute(paramURequest, paramURequest.mResponseClz);
    }
}