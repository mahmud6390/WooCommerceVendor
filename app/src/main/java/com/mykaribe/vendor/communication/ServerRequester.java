package com.mykaribe.vendor.communication;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.mykaribe.vendor.model.RequestModel;
import com.mykaribe.vendor.model.ResponseModel;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.MethodType;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


/**
 * Created by USER on 15/12/2017.
 */
public class ServerRequester {
    private final String TAG="ServerRequester";
    private final int CONNECTION_TIME_OUT = 30000;
    private final int READ_TIME_OUT = 30000;
    private Stack<RequestModel> stackUrl=new Stack<>();
    private AsyncServerRequest asyncServerRequest;
    private ServerRequester(){

    }
    private static ServerRequester instance;
    public static ServerRequester getInstance(){
        if(instance==null){
            instance=new ServerRequester();
        }
        return instance;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void processUrl(RequestModel requestModel){
        stackUrl.add(requestModel);
        asyncServerRequest=new AsyncServerRequest();
        asyncServerRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    private class AsyncServerRequest extends AsyncTask<Void, Void, ResponseModel>{

        @Override
        protected ResponseModel doInBackground(Void... params) {
            RequestModel requestModel=stackUrl.pop();
            String url=requestModel.getUrl();
            Logger.debugLog(TAG,"doInBackground>>>url:"+url);
            ResponseModel response =null;
            try {
                String res=sendHttpReq(url,requestModel.getMethodType());
                if(!TextUtils.isEmpty(res)){
                    response=new ResponseModel();
                    response.setResponse(res);
                    response.setListener(requestModel.getListener());
                    response.setUrl(url);
                    response.setOrderType(requestModel.getOrderType());
                }else{
                    response=new ResponseModel();
                    response.setUrl(url);
                    response.setListener(requestModel.getListener());
                    response.setOrderType(requestModel.getOrderType());
                }

            } catch (Exception e) {
                e.printStackTrace();
                response=new ResponseModel();
                response.setUrl(url);
                response.setListener(requestModel.getListener());
                response.setOrderType(requestModel.getOrderType());
            }

            return response;
        }

        @Override
        protected void onPostExecute(ResponseModel serverResponse) {
            if(serverResponse.getResponse()!=null){
                serverResponse.getListener().onRequestSuccess(serverResponse.getResponse(),serverResponse.getOrderType());
            }else{
                serverResponse.getListener().onRequestFail("Exception",serverResponse.getOrderType());
            }
            super.onPostExecute(serverResponse);
        }

        private String sendHttpReq(String reqUrl,String method) throws Exception{
            //reqUrl=reqUrl+"consumer_key="+Constant.CONSUMER_KEY+"&consumer_secret="+Constant.CONSUMER_SECRET;
            HttpURLConnection urlConnection = null;
            StringBuilder responseString = null;
            try {
                URL url = new URL(reqUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(method);
                Logger.debugLog(TAG,"sendHttpReq>>>reqUrl:"+reqUrl+":method:"+method);
                //with header and authentication
                String userCredentials = Constant.CONSUMER_KEY+":"+Constant.CONSUMER_SECRET;
                byte[] bytesEncoded = Base64.encodeBase64(userCredentials .getBytes());
                String authEncoded = new String(bytesEncoded);
                String basicAuth = "Basic " + authEncoded;
                urlConnection.setRequestProperty ("Authorization", basicAuth);
                urlConnection.setRequestProperty("Content-Type","application/json");
                //
                if(method.equals(MethodType.PUT.toString())){
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter outputStreamWriter=new OutputStreamWriter(urlConnection.getOutputStream());
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("status","completed");
                    Logger.debugLog(TAG,"sendHttpReq>>>putting>>>>>>>>>>>>"+jsonObject.toString());
                    outputStreamWriter.write(jsonObject.toString());
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    urlConnection.getInputStream();
                }
                //urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
                //urlConnection.setReadTimeout(READ_TIME_OUT);
                int statusCode = urlConnection.getResponseCode();
                Logger.debugLog(TAG,"statusCode:"+statusCode+"::reqUrl:"+reqUrl);
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    responseString = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        responseString.append(chunks);
                    }
                    Logger.debugLog(TAG, "sendHttpReq Sucs responseString " + responseString.toString());
                    return responseString.toString();
                } else {
                    Logger.debugLog(TAG, " Failed ");
                    return "";

                }
            } catch (IOException | InternalError e) {
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            throw new IOException ();

        }
    }
}
