/*
 * File Name: com.huawei.service.signalingDelivery.PostAsynCommand.java
 *
 * Copyright Notice:
 *      Copyright  1998-2008, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.huawei.service.signalingDelivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.utils.Constant;
import com.huawei.utils.HttpsUtil;
import com.huawei.utils.JsonUtil;
import com.huawei.utils.StreamClosedHttpResponse;

/**
 * Post Asynchronous Command to a specify device:
 * This interface is used by NAs to send messages to device.
 * If a device is not online,
 * the IoT platform buffers the message and delivers the message to the device after the device is online.
 * The NA can set the maximum buffering time.
 */
public class PostAsynCommandV4 {

    public static void main(String[] args) throws Exception {
        /*
         * the device must connect to IoT platform before na post asyn command to device
         */
//      {
//      "_class" : "com.huawei.iom.iocm.domain.nsse.NsseRoute",
//      "deviceId" : "8c23b6b4-ea68-48fb-9c2f-90452a81ebb1",
//      "appId" : "pAw9x9zinQnQkYSLWbiGI_O6iBUa",
//      "nsseId" : "http://185.11.1.43:8096",
//      "edgeGwId" : "MeterCig",
//      "edgeGwType" : "CoAP",
//      "status" : "ONLINE"
//  }

		// Two-Way Authentication
		HttpsUtil httpsUtil = new HttpsUtil();
		httpsUtil.initSSLConfigForTwoWay();

		// Authentication，get token
		String accessToken = login(httpsUtil);

		// Please make sure that the following parameter values have been modified in
		// the Constant file.
		String urlPostAsynCmd = Constant.POST_ASYN_CMD;
		String appId = Constant.APPID;
		
		// please replace the deviceId, when you use the demo.
		String deviceId = "e5064b02-c760-4770-b4d2-1041234758c6";
//        String callbackUrl = Constant.REPORT_CMD_EXEC_RESULT_CALLBACK_URL;
		String callbackUrl = "https://39.105.158.202:443/chinatelecom/reportCmdExecResult";

        //please replace the following parameter values, when you use the demo.
        //And those parameter values must be consistent with the content of profile that have been preset to IoT platform.
        //The following parameter values of this demo are use the watermeter profile that already initialized to IoT platform.
		String serviceId = "KeLucentyCommand";
		String method = "KE_LUCENTY_CMD";
		String[] cmds = {
//				"68D600D600684A90804E0A0A047201010208810015000121010104010110000101400001010114010104141E1E1E1E1E1E000000000000000000003516",
//				"68D600D600684A90804E0A0A047102010208C10015000121010104010110020101400201010116010104161E1E1E1E1E1E000000000000000000007D16",
//				"6816011601684A90804E0A0A0473040102084100150001210101080101010A0101010B0101080B0101010C0101040C0101100C0101400C0101010D1E1E1E1E1E1E000000000000000000004516"
//				,
				"6832003200685B90800200020C7101010103F216"
				};

		for (int i = 0; i < cmds.length; i++) {
			JSONObject json = new JSONObject();
			json.put("cmdValue", cmds[i]);
			ObjectNode paras = JsonUtil.convertObject2ObjectNode(json);

			Map<String, Object> paramCommand = new HashMap<>();
			paramCommand.put("serviceId", serviceId);
			paramCommand.put("method", method);
			paramCommand.put("paras", paras);

			Map<String, Object> paramPostAsynCmd = new HashMap<>();
			paramPostAsynCmd.put("deviceId", deviceId);
			paramPostAsynCmd.put("command", paramCommand);
			paramPostAsynCmd.put("callbackUrl", callbackUrl);

			String jsonRequest = JsonUtil.jsonObj2Sting(paramPostAsynCmd);

			Map<String, String> header = new HashMap<>();
			header.put(Constant.HEADER_APP_KEY, appId);
			header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

			HttpResponse responsePostAsynCmd = httpsUtil.doPostJson(urlPostAsynCmd, header, jsonRequest);

			String responseBody = httpsUtil.getHttpResponseBody(responsePostAsynCmd);

			System.out.println("PostAsynCommand, response content:");
			System.out.print(responsePostAsynCmd.getStatusLine());
			System.out.println(responseBody);
			System.out.println();
		}
    }

    /**
     * Authentication，get token
     * */
    @SuppressWarnings("unchecked")
    public static String login(HttpsUtil httpsUtil) throws Exception {

        String appId = Constant.APPID;
        String secret = Constant.SECRET;
        String urlLogin = Constant.APP_AUTH;

        Map<String, String> paramLogin = new HashMap<>();
        paramLogin.put("appId", appId);
        paramLogin.put("secret", secret);

        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);

        System.out.println("app auth success,return accessToken:");
        System.out.print(responseLogin.getStatusLine());
        System.out.println(responseLogin.getContent());
        System.out.println();

        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
        return data.get("accessToken");
    }

}
