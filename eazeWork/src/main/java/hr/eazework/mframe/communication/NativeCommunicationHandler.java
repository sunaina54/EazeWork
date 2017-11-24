package hr.eazework.mframe.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import hr.eazework.com.application.MyApplication;
import hr.eazework.com.ui.util.MLogger;
import hr.eazework.selfcare.communication.CommunicationManager;


//Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an Http Connection. Once the connection
// has been established, the AsyncTask downloads the contents of the server which is
// sent back to Listener by the AsyncTask's onPostExecute method.

public class NativeCommunicationHandler extends
		AsyncTask<RequestData, Void, ResponseData> implements IENUMCommunication {
    private static final String TAG = NativeCommunicationHandler.class.getName();
    Object objListener;
//	HttpPost httpPost;
//	HttpGet httpGet;
    HttpURLConnection connection;
	int conTimeOut;
	int conReqType = -1;
	int reqAPI_ID = -1;
	ResponseDataAcceptType acceptType = null;

	public NativeCommunicationHandler(int connectionTimeOut) {
		conTimeOut = connectionTimeOut;// 60000;//180000;
	}

	@SuppressWarnings("finally")
	@Override
	protected ResponseData doInBackground(RequestData... paramReq) {
		RequestData objReq = paramReq[0];
		ResponseData responseData=new ResponseData();
		String retVal = "";
		try {
			responseData.setRequestData(objReq);
			objListener = objReq.getAct();
			conReqType = objReq.getReqType();
			reqAPI_ID = objReq.getReqApiId();
			acceptType = objReq.getAcceptType();
			retVal = new AppResponseXML().getConnectionErrorXML(acceptType);
            Log.d(TAG,"This Service is called " + objReq.getRequestUrl() + " at " + objListener.toString());

            if (objReq.getSecureConnection()) {
				// /SEND HTTPS REQUEST
			} else {
				if (conReqType == REQ_POST) {
					if (objReq.getRequestUrl().indexOf("https") > -1) {
						retVal = sendHttpPostRequest(objReq.getRequestUrl(),
								objReq.getRequestXML(), objReq.getHeaderList());
					} else {
						retVal = sendHttpPostRequest(objReq.getRequestUrl(),
								objReq.getRequestXML(), objReq.getHeaderList());
					}
				} else if (conReqType == REQ_GET) {
					// retVal = sendHttpGetRequest(objReq.getRequestUrl(),
					// objReq.getRequestXML(), objReq.getHeaderList());
	//				retVal = sendHttpGetRequest(objReq.getRequestUrl());
                    Log.d(MyApplication.TAG,"Get not handled");
                }
				if(retVal!=null)
				responseData.setSuccess(true);
			}
		} catch (IOException e) {
			retVal = new AppResponseXML().getConnectionErrorXML(acceptType);
		} 
		responseData.setResponceData(retVal);
		return responseData;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(ResponseData result) {
		new CommunicationManager().sendReponseToUIController(objListener,
				result);
	}

	// SEND POST REQUEST TO SERVER
	// Input URL, XML, HEADERLIST
	// Output response from server
	@SuppressWarnings("finally")
    private String sendHttpPostRequest(String strUrl, String parameters,Hashtable<String, String> headerTable) throws IOException {
        return sendHttpRequest(strUrl,parameters,headerTable,"POST","application/json");
    }

    public String sendHttpGetRequest(String strUrl, String parameters,Hashtable<String, String> headerTable,String contentType) throws IOException {
        return sendHttpRequest(strUrl,parameters,headerTable,"GET",contentType);
    }
    private String sendHttpRequest(String strUrl, String parameters,Hashtable<String, String> headerTable,String methodType,String contentType) throws IOException {
        //MLogger.debug(getClass().getName(), "Sending Request.." + url);
        //MLogger.debug(getClass().getName(), "Request XML.." + xml);
        TrustManager[] trustAllCerts = new TrustManager[]
                {
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {

                            }
                            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        }
                };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response ="";
        HttpURLConnection connection = null;
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(methodType);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("User-Agent", "mozilla");//


            if(!TextUtils.isEmpty(parameters))
            {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(parameters);
                writer.flush();
                writer.close();
                os.close();
            }
            connection.connect();
            int resposeCode = connection.getResponseCode();
            if(resposeCode == 200) {
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine())!=null)
                {
                    buffer.append(line);
                }
                response = buffer.toString();
            } else{
                InputStream stream = connection.getErrorStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                response = buffer.toString();
            }
            //          System.out.println(connection.getResponseCode() + "This is response code " + connection.getResponseMessage() + "params " + parameters);


        } catch (MalformedURLException e) {
            Log.d(MyApplication.TAG,"Malformed URL");

        } catch (IOException e) {
            Log.d(MyApplication.TAG,"IO Exception");
        } finally {
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(reader !=null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.d(MyApplication.TAG,"IO Exception");
            }
            return response;
        }
    }


	/*private String sendHttpPostRequest(String url, String xml,
			Hashtable<String, String> headerTable) throws IOException {
		//MLogger.debug(getClass().getName(), "Sending Request.." + url);
		//MLogger.debug(getClass().getName(), "Request XML.." + xml);
		String returnVal = "";

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, conTimeOut);
		HttpConnectionParams.setSoTimeout(httpParameters, conTimeOut);
		ConnManagerParams.setTimeout(httpParameters, conTimeOut);

		// HttpClient httpClient = new DefaultHttpClient();
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		httpPost = new HttpPost(url);
		HttpResponse response = null;
		int respErrorCode = 0;
		StringEntity tmp = null;
		setHeaders(headerTable);
		try {
			tmp = new StringEntity(xml, "UTF-8");
			httpPost.setEntity(tmp); // ADD XML TO POST BODY
			response = httpClient.execute(httpPost);
			int respCode = response.getStatusLine().getStatusCode();
			respErrorCode = respCode;
			if (response != null && respCode == HttpURLConnection.HTTP_OK) {
				returnVal = EntityUtils.toString(response.getEntity());
				String allAPI = "URL called is: " + url + " \n XML called is:"
						+ xml + " \n Response is: " + returnVal;
				MLogger.debug("Response Recieved", allAPI);
			} else if (respCode == HttpURLConnection.HTTP_NOT_FOUND) {
				returnVal = new AppResponseXML()
						.getConnectionNOTFOUNDXML(acceptType);
			} else {
				String error = "";
				InputStream io = response.getEntity().getContent();
				error = io.toString();
				error = response.getStatusLine().getReasonPhrase();
				MLogger.debug(getClass().getName(), "response:" + error);
				error = EntityUtils.toString(response.getEntity());
				MLogger.debug(getClass().getName(), "response:" + error);
				throw new CommunicationError(respCode);
			}
			if (returnVal.trim().equalsIgnoreCase("")) {
				throw new CommunicationError(respCode);
			}
		} catch (CommunicationError e) {
			MLogger.error(getClass().getName(), e.getMessage());
			returnVal =null;
			// new AppResponseXML().getConnectionErrorXML(acceptType);
			MLogger.error(getClass().getName(), "URL called is: " + url
					+ " \n XML called is:" + xml + " \n Error Response is: "
					+ returnVal + e.getMessage() + " error code: "
					+ respErrorCode);
		} catch (UnsupportedEncodingException e) {
			MLogger.error(getClass().getName(), e.getMessage());
			MLogger.debug(getClass().getName(), "CONNECTION ERROR:" + e);
			Log.e("Your App Name Here",
					"HttpUtils : UnsupportedEncodingException : " + e);
			returnVal =null;
			// new AppResponseXML().getConnectionErrorXML(acceptType);
			MLogger.error(getClass().getName(), "URL called is: " + url
					+ " \n XML called is:" + xml + " \n Error Response is: "
					+ returnVal + e.getMessage() + " error code: "
					+ respErrorCode);
		} catch (IOException e) {
			MLogger.error(getClass().getName(), e.getMessage());
			returnVal =null;
			// new AppResponseXML().getConnectionErrorXML(acceptType);
			MLogger.error(getClass().getName(), "URL called is: " + url
					+ " \n XML called is:" + xml + " \n Error Response is: "
					+ returnVal + e.getMessage() + " error code: "
					+ respErrorCode);
		} catch (ParseException e) {
			MLogger.error(getClass().getName(), e.getMessage());
			returnVal =null;
			// new AppResponseXML().getConnectionErrorXML(acceptType);
			MLogger.error(getClass().getName(), "URL called is: " + url
					+ " \n XML called is:" + xml + " \n Error Response is: "
					+ returnVal + e.getMessage() + " error code: "
					+ respErrorCode);
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
			Log.e("Your App Name Here", "HttpUtils: " + e);
			returnVal =null;
			// new AppResponseXML().getConnectionErrorXML(acceptType);
			MLogger.error(getClass().getName(), "URL called is: " + url
					+ " \n XML called is:" + xml + " \n Error Response is: "
					+ returnVal + e.getMessage() + " error code: "
					+ respErrorCode);
		} finally {
			if (httpPost != null) {

			}
			return returnVal;
		}
	}*/

	//@SuppressWarnings("finally")
	/*private String sendHttpsPostRequest(String url, String xml,
			Hashtable<String, String> headerTable) throws IOException {
		MLogger.debug(getClass().getName(), "Sending HTTPS Request.." + url);
		MLogger.debug(getClass().getName(), "Request XML.." + xml);
		String returnVal = "";

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, conTimeOut);
		HttpConnectionParams.setSoTimeout(httpParameters, conTimeOut);
		ConnManagerParams.setTimeout(httpParameters, conTimeOut);

		HttpClient httpClient = getNewHttpClient();
		httpPost = new HttpPost(url);
		HttpResponse response = null;
		StringEntity tmp = null;
		setHeaders(headerTable);
		try {
			tmp = new StringEntity(xml, "UTF-8");
			httpPost.setEntity(tmp); // ADD XML TO POST BODY
			response = httpClient.execute(httpPost);
			int respCode = response.getStatusLine().getStatusCode();
			if (response != null && respCode == HttpURLConnection.HTTP_OK) {
				returnVal = EntityUtils.toString(response.getEntity());
				String allAPI = "URL called is: " + url + " \n XML called is:"
						+ xml + " \n Response is: " + returnVal;
				MLogger.debug("Response Recieved", allAPI);
			} else if (respCode == HttpURLConnection.HTTP_NOT_FOUND) {
				returnVal = new AppResponseXML()
						.getConnectionNOTFOUNDXML(acceptType);
			} else {
				String error = "";
				InputStream io = response.getEntity().getContent();
				error = io.toString();
				error = response.getStatusLine().getReasonPhrase();
				MLogger.debug(getClass().getName(), "response:" + error);
				error = EntityUtils.toString(response.getEntity());
				MLogger.debug(getClass().getName(), "response:" + error);
				throw new CommunicationError(respCode);
			}
			if (returnVal.trim().equalsIgnoreCase("")) {
				throw new CommunicationError(respCode);
			}
		} catch (CommunicationError e) {
			MLogger.error(getClass().getName(), e.getMessage());
			returnVal = null;
		} catch (UnsupportedEncodingException e) {
			MLogger.error(getClass().getName(), e.getMessage());
			MLogger.debug(getClass().getName(), "CONNECTION ERROR:" + e);
			Log.e("Your App Name Here",
					"HttpUtils : UnsupportedEncodingException : " + e);
			returnVal = new AppResponseXML().getConnectionErrorXML(acceptType);
		} catch (IOException e) {
			MLogger.error(getClass().getName(), e.getMessage());
//			returnVal = new AppResponseXML().getConnectionErrorXML(acceptType);
			returnVal = null;
		} catch (ParseException e) {
			MLogger.error(getClass().getName(), e.getMessage());
//			returnVal = new AppResponseXML().getConnectionErrorXML(acceptType);
			returnVal = null;
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
			Log.e("Your App Name Here", "HttpUtils: " + e);
//			returnVal = new AppResponseXML().getConnectionErrorXML(acceptType);
			returnVal = null;
		} finally {
			if (httpPost != null) {

			}
			return returnVal;
		}
	}*/

	// SEND GET REQUEST TO SERVER
	// Input URL, XML, HEADERLIST
	// Output response from server
	//@SuppressWarnings("finally")
	/*private String sendHttpGetRequest(String url) throws IOException {
		MLogger.debug(getClass().getName(), "Sending Request.." + url);
		MLogger.debug("Sending Request", "");
		String returnVal = "";

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, conTimeOut);
		HttpConnectionParams.setSoTimeout(httpParameters, conTimeOut);

		// HttpClient httpClient = new DefaultHttpClient();
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpGet = new HttpGet(url);
		HttpResponse response = null;
		// setHeaders(headerTable);
		try {
			// MLogger.debug(getClass().getName(),"URL::"+url);
			// MLogger.debug(getClass().getName(),"XML::"+xml);
			response = httpClient.execute(httpGet);// , localContext);
			int respCode = response.getStatusLine().getStatusCode();
			if (response != null && respCode == HttpURLConnection.HTTP_OK) {
				returnVal = EntityUtils.toString(response.getEntity());
				MLogger.debug("result", returnVal);
			}
		}

		catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
			Log.e("Your App Name Here", "HttpUtils: " + e);
			// returnVal = new AppResponseXML().getConnectionErrorXML();
		} finally {
			MLogger.debug("Response Received", returnVal);
			if (httpGet != null) {

			}
			return returnVal;
		}
	}*/

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	@SuppressWarnings("unused")
	private String sendURLRequest(String myurl, String xml) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 500;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			MLogger.debug("DEBUG", "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readIt(is, len);
			return contentAsString;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);
		return new String(buffer);
	}

	/**
	 * @param //hdr
	 */
	/*void setHeaders(Hashtable<String, String> hdr) {
		if (hdr == null)
			return;
		try {
			Enumeration<String> enumHdr = hdr.keys();
			while (enumHdr.hasMoreElements()) {
				String key = "" + enumHdr.nextElement();
				String val = hdr.get(key);
				String accept = "ACCEPT";
				if (!key.trim().equalsIgnoreCase(accept)) {
					if (conReqType == REQ_POST) {
						*//*MLogger.debug(getClass().getName(), "POST REQ HDR:"
								+ " Key=" + key + " Val=" + val);*//*
						httpPost.setHeader(key, val);

					} else if (conReqType == REQ_GET) {
						httpGet.setHeader(key, val);
					}
				} else {
					val = acceptType.getAcceptTypeToken();
					if (conReqType == REQ_POST) {
						httpPost.setHeader(accept, val);
					} else if (conReqType == REQ_GET) {
						httpGet.setHeader(accept, val);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings("serial")
	class CommunicationError extends Exception {
		int errorCd;

		public CommunicationError(int errorCode) {
			// TODO Auto-generated constructor stub
			errorCd = errorCode;
		}

		public int getErrorCode() {
			return errorCd;
		}

	}

	public HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}*/

	/*public class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
*/
}
