///*
// * File Name: com.huawei.service.signalingDelivery.PostAsynCommand.java
// *
// * Copyright Notice:
// *      Copyright  1998-2008, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
// *
// *      Warning: This computer software sourcecode is protected by copyright law
// *      and international treaties. Unauthorized reproduction or distribution
// *      of this sourcecode, or any portion of it, may result in severe civil and
// *      criminal penalties, and will be prosecuted to the maximum extent
// *      possible under the law.
// */
//package com.huawei.service.signalingDelivery;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.utils.DateUtils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.huawei.utils.Constant;
//import com.huawei.utils.HttpsUtil;
//import com.huawei.utils.JsonUtil;
//import com.huawei.utils.StreamClosedHttpResponse;
//
///**
// * Post Asynchronous Command to a specify device:
// * This interface is used by NAs to send messages to device.
// * If a device is not online,
// * the IoT platform buffers the message and delivers the message to the device after the device is online.
// * The NA can set the maximum buffering time.
// */
//public class BatchCommand {
//
//    public static void main(String[] args) throws Exception {
//        /*
//         * the device must connect to IoT platform before na post asyn command to device
//         */
////      {
////      "_class" : "com.huawei.iom.iocm.domain.nsse.NsseRoute",
////      "deviceId" : "8c23b6b4-ea68-48fb-9c2f-90452a81ebb1",
////      "appId" : "pAw9x9zinQnQkYSLWbiGI_O6iBUa",
////      "nsseId" : "http://185.11.1.43:8096",
////      "edgeGwId" : "MeterCig",
////      "edgeGwType" : "CoAP",
////      "status" : "ONLINE"
////  }
//    	
//    	
//
//        // Two-Way Authentication
//        HttpsUtil httpsUtil = new HttpsUtil();
//        httpsUtil.initSSLConfigForTwoWay();
//
//        // Authentication，get token
//        String accessToken = login(httpsUtil);
//
//        //Please make sure that the following parameter values have been modified in the Constant file.
//        String urlPostAsynCmd = Constant.POST_ASYN_CMD;
//        String appId = Constant.APPID;
//
//        //please replace the deviceId, when you use the demo.
//        String deviceId = "9459e7b6-d1a7-4474-aa9c-210db6c2dbdc";
////        String callbackUrl = Constant.REPORT_CMD_EXEC_RESULT_CALLBACK_URL;
//        String callbackUrl = "https://222.222.60.178:18213/reportCmdExecResult";
//
//        //please replace the following parameter values, when you use the demo.
//        //And those parameter values must be consistent with the content of profile that have been preset to IoT platform.
//        //The following parameter values of this demo are use the watermeter profile that already initialized to IoT platform.
//        String serviceId = "SettingReportPeriod";
//        String method = "SET_REPORT_PERIOD";
////        {"AFN":19 ,"IMSI":"xxx" ,"CNT":"xxx", "DIR":"xxx","ReportBaseTime":"xxx"," ReportIntervalHours":"xxx"}
//        
//        
//        serviceId = "SettingValveState";
//        method = "SET_VALVE_STATE";
//        
////        {"appId":"YXnpwWndC3ioc15OGALXZvHLB1oa","secret":"YXnpwWndC3ioc15OGALXZvHLB1oa",
////        	* "deviceList:[232132,34324,34534,456456]",
////        	 * "commandId":"1",operatorId":"1", "serviceId":"ACD",method="CDE" "param":"{\"value\":\"12\"}"
//        
//        JSONObject json = new JSONObject();
//        
//        
//        json.put("appId", Constant.APPID);
//        json.put("secret", Constant.SECRET);
//        
//        List<String> deviceList = new ArrayList<>();
//        deviceList.add("9459e7b6-d1a7-4474-aa9c-210db6c2dbdc");
//        deviceList.add("a0279c34-76b7-44db-81ce-80d361aa3257");
//        json.put("deviceList", deviceList);
//        json.put("commandId", "1");
//        json.put("operatorId", 3);
//        json.put("serviceId", serviceId);
//        json.put("method", method);
//        
//        JSONObject param = new JSONObject();
//        param.put("ValveOperate", 1);
//        json.put("param", param.toString());
//
////        json.put("ReportBaseTime", DateUtils.formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));
////        json.put("ReportIntervalHours", 1);
////        
////        json.put("ValveOperate", 1);
//
//        ObjectNode paras = JsonUtil.convertObject2ObjectNode(json);
//      
////        Map<String, Object> paramCommand = new HashMap<>();
////        paramCommand.put("serviceId", serviceId);
////        paramCommand.put("method", method);
////        paramCommand.put("paras", paras);      
//        
////        Map<String, Object> paramPostAsynCmd = new HashMap<>();
////        paramPostAsynCmd.put("deviceId", deviceId);
////        paramPostAsynCmd.put("command", paramCommand);
////        paramPostAsynCmd.put("callbackUrl", callbackUrl);
//        
////        String jsonRequest = JsonUtil.jsonObj2Sting(paramPostAsynCmd);
//                
//        Map<String, String> header = new HashMap<>();
//        header.put(Constant.HEADER_APP_KEY, appId);
//        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
//        
//        HttpResponse responsePostAsynCmd = httpsUtil.doPostJson(urlPostAsynCmd, header, jsonRequest);
//
//        String responseBody = httpsUtil.getHttpResponseBody(responsePostAsynCmd);
//
//        System.out.println("PostAsynCommand, response content:");
//        System.out.print(responsePostAsynCmd.getStatusLine());
//        System.out.println(responseBody);
//        System.out.println();
//    }
//
//    /**
//     * Authentication，get token
//     * */
//    @SuppressWarnings("unchecked")
//    public static String login(HttpsUtil httpsUtil) throws Exception {
//
//        String appId = Constant.APPID;
//        String secret = Constant.SECRET;
//        String urlLogin = Constant.APP_AUTH;
//
//        Map<String, String> paramLogin = new HashMap<>();
//        paramLogin.put("appId", appId);
//        paramLogin.put("secret", secret);
//
//        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);
//
//        System.out.println("app auth success,return accessToken:");
//        System.out.print(responseLogin.getStatusLine());
//        System.out.println(responseLogin.getContent());
//        System.out.println();
//
//        Map<String, String> data = new HashMap<>();
//        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
//        return data.get("accessToken");
//    }
//
//}
