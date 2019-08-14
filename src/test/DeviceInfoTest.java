package test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.huawei.utils.Constant;
import com.huawei.utils.HttpsUtil;
import com.huawei.utils.JsonUtil;
import com.huawei.utils.StreamClosedHttpResponse;

/**
 * Set device information:
 * This interface is used by an NA to set or modify device information.
 */
public class DeviceInfoTest {

	public static void main(String args[]) throws Exception {

		//
		String response = "{\"status\":-1,\"error\":\"重复告警\"}";
		response = response.replace("\\", "");
		System.out.println(JSONObject.parseObject(response));
//        // Two-Way Authentication
//        HttpsUtil httpsUtil = new HttpsUtil();
//        httpsUtil.initSSLConfigForTwoWay();
//
//        // Authentication，get token
//        String accessToken = login(httpsUtil);
//
//        //Please make sure that the following parameter values have been modified in the Constant file.
//		String appId = Constant.APPID;
//
//        //please replace the deviceId, when you use the demo.
//        String deviceId = "4ea75ef9-7469-4136-ae9c-b900719a2bf6";
//        String urlModifyDeviceInfo = Constant.MODIFY_DEVICE_INFO + "/" + deviceId;
//
//        //please replace the following parameter values, when you use the demo.
//        //And those parameter values must be consistent with the content of profile that have been preset to IoT platform.
//        //The following parameter values of this demo are use the watermeter profile that already initialized to IoT platform.
//        String manufacturerId= "LiteNAdemo";
//        String manufacturerName = "LiteNAdemo";
//        String deviceType = "WaterMeter";
//        String model = "demo130";
//        String protocolType = "CoAP";
//
//        Map<String, Object> paramModifyDeviceInfo = new HashMap<>();
////        paramModifyDeviceInfo.put("manufacturerId", manufacturerId);
////        paramModifyDeviceInfo.put("manufacturerName", manufacturerName);
////        paramModifyDeviceInfo.put("deviceType", deviceType);
////        paramModifyDeviceInfo.put("model", model);
////        paramModifyDeviceInfo.put("protocolType", protocolType);
//        
//        
//        String verifyCode = "865352030465792";
//		String nodeId = verifyCode;
//        Integer timeout = 0;
//        Map<String, Object> paramReg = new HashMap<>();
//        paramReg.put("verifyCode", verifyCode.toUpperCase());
//        paramReg.put("nodeId", nodeId.toUpperCase());
//        paramReg.put("timeout", timeout);
//
//        String jsonRequest = JsonUtil.jsonObj2Sting(paramReg);
//
//        Map<String, String> header = new HashMap<>();
//        header.put(Constant.HEADER_APP_KEY, appId);
//        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
//        header.put("Content-Type", "application/json");
//        urlModifyDeviceInfo = Constant.BASE_URL + "/iocm/app/reg/v1.1.0/deviceCredentials";
//
//        StreamClosedHttpResponse responseModifyDeviceInfo = httpsUtil.doPostJsonGetStatusLine(urlModifyDeviceInfo,
//        		header,jsonRequest);
//
//        System.out.println("ModifyDeviceInfo, response content:");
//        System.out.print(responseModifyDeviceInfo.getStatusLine());
//        System.out.println(responseModifyDeviceInfo.getContent());
//        System.out.println();
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