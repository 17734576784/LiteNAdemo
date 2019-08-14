package com.huawei.utils;

/**   
* @Title: HTTPSClientUtil.java 
* @Package utils 
* @Description: TODO(用一句话描述该文件做什么) 
* @author dbr
* @date 2018年12月26日 下午4:27:18 
* @version V1.0   
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

/**
 * @ClassName: HTTPSClientUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author dbr
 * @date 2018年12月26日 下午4:27:18
 * 
 */
public class HttpsClientUtil {
	
	public final static String HTTPGET = "GET";

	public final static String HTTPPUT = "PUT";

	public final static String HTTPPOST = "POST";

	public final static String HTTPDELETE = "DELETE";

	public final static String HTTPACCEPT = "Accept";

	public final static String CONTENT_LENGTH = "Content-Length";

	public final static String CHARSET_UTF8 = "UTF-8";
	
	
	/** 
	* @Title: sslClientByCert 
	* @Description: 创建证书访问的 HttpClient
	* @param @return
	* @param @throws Exception    设定文件 
	* @return HttpClient    返回类型 
	* @throws 
	*/
	public HttpClient sslClientByCert() throws Exception {
		// 1 Import your own certificate
		String demo_base_Path = System.getProperty("user.dir");
		String selfcertpath = demo_base_Path + Constant.SELFCERTPATH;
		String trustcapath = demo_base_Path + Constant.TRUSTCAPATH;

		KeyStore selfCert = KeyStore.getInstance("pkcs12");
		selfCert.load(new FileInputStream(selfcertpath), Constant.SELFCERTPWD.toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
		kmf.init(selfCert, Constant.SELFCERTPWD.toCharArray());

		// 2 Import the CA certificate of the server,
		KeyStore caCert = KeyStore.getInstance("jks");
		caCert.load(new FileInputStream(trustcapath), Constant.TRUSTCAPWD.toCharArray());
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("sunx509");
		trustManagerFactory.init(caCert);

		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslcontext,
				NoopHostnameVerifier.INSTANCE);
		// 创建Registry
		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
				.setExpectContinueEnabled(Boolean.TRUE)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
		
  		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();
		// 创建ConnectionManager，添加Connection配置信息
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry);
		CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfig).build();
		return closeableHttpClient;
	}
	
	/** 
	* @Title: sslClient 
	* @Description: 创建无证书访问的 HttpClient
	* @param @return
	* @param @throws Exception    设定文件 
	* @return HttpClient    返回类型 
	* @throws 
	*/
	public HttpClient sslClient() throws Exception {
		CloseableHttpClient closeableHttpClient = null;
		try {
			SSLContext sslcontext = SSLContext.getInstance("SSL");
			//鍒涘缓TrustManager
			X509TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			
			sslcontext.init(null, new TrustManager[] { tm }, null);
			
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslcontext,
					NoopHostnameVerifier.INSTANCE);
			// 创建Registry
			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
					.setExpectContinueEnabled(Boolean.TRUE)
					.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
					.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
			
	  		// 设置协议http和https对应的处理socket链接工厂的对象
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();
			// 创建ConnectionManager，添加Connection配置信息
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager)
					.setDefaultRequestConfig(requestConfig).build();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return closeableHttpClient;
	}
	
	public HttpResponse doPostJson(String url, Map<String, String> headerMap, String content) {
		HttpPost request = new HttpPost(url);
		addRequestHeader(request, headerMap);

		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

		return executeHttpRequest(request);
	}
	
	public StreamClosedHttpResponse doPostJsonGetStatusLine(String url, Map<String, String> headerMap, String content) {
		HttpPost request = new HttpPost(url);
		addRequestHeader(request, headerMap);

		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

		HttpResponse response = executeHttpRequest(request);
		if (null == response) {
			System.out.println("The response body is null.");
		}

		return (StreamClosedHttpResponse) response;
	}
	
	public StreamClosedHttpResponse doPostJsonGetStatusLine(String url, String content) {
		HttpPost request = new HttpPost(url);

		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

		HttpResponse response = executeHttpRequest(request);
		if (null == response) {
			System.out.println("The response body is null.");
		}

		return (StreamClosedHttpResponse) response;
	}
	
	public StreamClosedHttpResponse doPostFormUrlEncodedGetStatusLine(String url, Map<String, String> formParams)
			throws Exception {
		HttpPost request = new HttpPost(url);

		request.setEntity(new UrlEncodedFormEntity(paramsConverter(formParams)));

		HttpResponse response = executeHttpRequest(request);
		if (null == response) {
			System.out.println("The response body is null.");
			throw new Exception();
		}

		return (StreamClosedHttpResponse) response;
	}

	public StreamClosedHttpResponse doPostMultipartFile(String url, Map<String, String> headerMap, File file) {
		HttpPost request = new HttpPost(url);
		addRequestHeader(request, headerMap);

		FileBody fileBody = new FileBody(file);
		// Content-Type:multipart/form-data;
		// boundary=----WebKitFormBoundarypJTQXMOZ3dLEzJ4b
		HttpEntity reqEntity = (HttpEntity) MultipartEntityBuilder.create().addPart("file", fileBody).build();
		request.setEntity(reqEntity);

		return (StreamClosedHttpResponse) executeHttpRequest(request);
	}
	
	public HttpResponse doPutJson(String url, Map<String, String> headerMap, String content) {
		HttpPut request = new HttpPut(url);
		addRequestHeader(request, headerMap);

		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

		return executeHttpRequest(request);
	}
	
	public HttpResponse doPut(String url, Map<String, String> headerMap) {
		HttpPut request = new HttpPut(url);
		addRequestHeader(request, headerMap);

		return executeHttpRequest(request);
	}

	public StreamClosedHttpResponse doPutJsonGetStatusLine(String url, Map<String, String> headerMap, String content) {
		HttpResponse response = doPutJson(url, headerMap, content);
		if (null == response) {
			System.out.println("The response body is null.");
		}

		return (StreamClosedHttpResponse) response;
	}

	public StreamClosedHttpResponse doPutGetStatusLine(String url, Map<String, String> headerMap) {
		HttpResponse response = doPut(url, headerMap);
		if (null == response) {
			System.out.println("The response body is null.");
		}

		return (StreamClosedHttpResponse) response;
	}
	
	public HttpResponse doGetWithParas(String url, Map<String, String> queryParams, Map<String, String> headerMap)
			throws Exception {
		HttpGet request = new HttpGet();
		addRequestHeader(request, headerMap);

		URIBuilder builder;
		try {
			builder = new URIBuilder(url);
		} catch (URISyntaxException e) {
			System.out.printf("URISyntaxException: {}", e);
			throw new Exception(e);

		}

		if (queryParams != null && !queryParams.isEmpty()) {
			builder.setParameters(paramsConverter(queryParams));
		}
		request.setURI(builder.build());

		return executeHttpRequest(request);
	}

	public StreamClosedHttpResponse doGetWithParasGetStatusLine(String url, Map<String, String> queryParams,
			Map<String, String> headerMap) throws Exception {
		HttpResponse response = doGetWithParas(url, queryParams, headerMap);
		if (null == response) {
			System.out.println("The response body is null.");
		}

		return (StreamClosedHttpResponse) response;
	}

	public HttpResponse doDelete(String url, Map<String, String> headerMap) {
		HttpDelete request = new HttpDelete(url);
		addRequestHeader(request, headerMap);

		return executeHttpRequest(request);
	}

	public StreamClosedHttpResponse doDeleteGetStatusLine(String url, Map<String, String> headerMap) {
		HttpResponse response = doDelete(url, headerMap);
		if (null == response) {
			System.out.println("The response body is null.");
		}

		return (StreamClosedHttpResponse) response;
	}
	
	private List<NameValuePair> paramsConverter(Map<String, String> params) {
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		Set<Map.Entry<String, String>> paramsSet = params.entrySet();
		for (Map.Entry<String, String> paramEntry : paramsSet) {
			nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
		}

		return nvps;
	}
	
	private static void addRequestHeader(HttpUriRequest request, Map<String, String> headerMap) {
		if (headerMap == null) {
			return;
		}

		for (String headerName : headerMap.keySet()) {
			if (CONTENT_LENGTH.equalsIgnoreCase(headerName)) {
				continue;
			}

			String headerValue = headerMap.get(headerName);
			request.addHeader(headerName, headerValue);
		}
	}

	private HttpResponse executeHttpRequest(HttpUriRequest request) {
		HttpResponse response = null;

		try {
			HttpClient httpClient = sslClient();
			response = httpClient.execute(request);
		} catch (Exception e) {
			System.out.println("executeHttpRequest failed.");
		} finally {
			try {
				response = new StreamClosedHttpResponse(response);
			} catch (IOException e) {
				System.out.println("IOException: " + e.getMessage());
			}
		}

		return response;
	}
	
	public String getHttpResponseBody(HttpResponse response)
			throws UnsupportedOperationException, IOException {
		if (response == null) {
			return null;
		}

		String body = null;

		if (response instanceof StreamClosedHttpResponse) {
			body = ((StreamClosedHttpResponse) response).getContent();
		} else {
			HttpEntity entity = response.getEntity();
			if (entity != null && entity.isStreaming()) {
				String encoding = entity.getContentEncoding() != null ? entity
						.getContentEncoding().getValue() : null;
				body = StreamUtil.inputStream2String(entity.getContent(),
						encoding);
			}
		}

		return body;
	}
	
}
