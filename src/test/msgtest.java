/**   
* @Title: msgtest.java 
* @Package test 
* @Description: TODO(用一句话描述该文件做什么) 
* @author dbr
* @date 2019年4月15日 上午8:59:07 
* @version V1.0   
*/
package test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.DateUtils;

import com.huawei.utils.HttpsClientUtil;

/**
 * @ClassName: msgtest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author dbr
 * @date 2019年4月15日 上午8:59:07
 * 
 */
public class msgtest {

	/**
	 * @throws Exception @Title: main @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param @param args 设定文件 @return void 返回类型 @throws
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println(DateUtils.formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));
		
		
		// TODO Auto-generated method stub
		HttpsClientUtil https = new HttpsClientUtil();
		https.sslClient();
		String url = "https://sdkulink.028lk.com:8082/Api/SendSms";

//		 ?LoginName=*&Pwd=*&FeeType=*&Mobile=*&Content=*&SignName=&TimingDate=&ExtCode=
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("LoginName", "SN00046");
		queryParams.put("Pwd", "ul@2019");
		queryParams.put("FeeType", "2");
		queryParams.put("Mobile", "17734576784");
		queryParams.put("Content", "hello");
		queryParams.put("SignName", "");
		queryParams.put("TimingDate", "20190415101010");
		queryParams.put("ExtCode", "");

		HttpResponse httpResponse = https.doGetWithParas(url, queryParams, null);

		String bodySubscribe = https.getHttpResponseBody(httpResponse);

		System.out.println(httpResponse.getStatusLine());
		System.out.println(bodySubscribe);

	}

}
