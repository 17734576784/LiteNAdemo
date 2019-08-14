package com.keyd.test;

import java.util.HashMap;
import java.util.Map;

import com.huawei.utils.Constant;
import com.huawei.utils.HttpsUtil;
import com.huawei.utils.JsonUtil;
import com.huawei.utils.StreamClosedHttpResponse;

/**
 * This interface is used by NAs to query delivered commands.
 */
public class QueryDeviceHistoryData {


	public static void main(String args[]) throws Exception {

        // Two-Way Authentication
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        // Authentication，get token
        String accessToken = login(httpsUtil);

        //Please make sure that the following parameter values have been modified in the Constant file.
		String appId = Constant.APPID;
        String urlQueryDeviceHistoryData = Constant.QUERY_DEVICE_HISTORY_DATA;
        
        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, appId);
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("deviceId", "a0279c34-76b7-44db-81ce-80d361aa3257");
        queryParams.put("gatewayId", "a0279c34-76b7-44db-81ce-80d361aa3257");
        queryParams.put("serviceId", "ReportBeforeOpenAccount");
        queryParams.put("pageSize", "1000");
		queryParams.put("startTime", "20190502T160750Z");

        
        
		StreamClosedHttpResponse responseQueryDeviceCMD = httpsUtil
				.doGetWithParasGetStatusLine(urlQueryDeviceHistoryData, queryParams, header);
        
        System.out.println("QueryAsynCommands, response content:");
		System.out.println(responseQueryDeviceCMD.getStatusLine().getStatusCode());
		System.out.println(responseQueryDeviceCMD.getContent());
        System.out.println();
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