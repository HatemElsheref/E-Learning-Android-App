package com.example.finalapp.Helper;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class Kernal {

    public static BufferedReader preparePostHeaders(HttpURLConnection connection,String requestbody){
        try{
//            connection.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Accept", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (!requestbody.isEmpty()){
                connection.setRequestProperty("Content-Length",""+Integer.toString(requestbody.getBytes().length));
                DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
                wr.writeBytes(requestbody);
                wr.flush();
                wr.close();
            }

            InputStream is = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            return bufferedReader;
        }catch (Exception e){
            System.out.println(e.getMessage()+"*******************");
            return  null;
        }
    }
    public static JSONObject sendPostRequest(String Link,JSONObject requestbody,String token){
        try{
            URL url=new URL(Link);
            BufferedReader bufferedReader;
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
//            if (token!=null){
//                connection.setRequestProperty("Authorization","bearer"+token);
//            }
            if(requestbody!=null){
                String encoded=Kernal.Encoder(requestbody);
                 bufferedReader=Kernal.preparePostHeaders(connection,encoded);
            }else{
                 bufferedReader=Kernal.preparePostHeaders(connection,"");
            }
            JSONObject jsonObject=new JSONObject(bufferedReader.readLine());
            return jsonObject;
        }catch (Exception e){
            return  null;
        }
    }
    public static JSONObject sendResourceRequest(String Link,String method,JSONObject requestbody){
        try{
            URL url=new URL(Link);
            BufferedReader bufferedReader;
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            String activeDeleteMethod="";
            if (requestbody!=null){
                activeDeleteMethod+=Encoder(requestbody);
            }else{
                activeDeleteMethod="_method="+method;
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length",Integer.toString(activeDeleteMethod.getBytes().length));
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
            wr.writeBytes(activeDeleteMethod);
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            JSONObject jsonObject=new JSONObject(bufferedReader.readLine());
            return  jsonObject;

        }catch (Exception e){
            System.out.println(e.getMessage()+"==============>>exception of connection");
            return  null;
        }
    }
    public static JSONArray sendGetRequest(String Link){
        try{
            URL APIURL=new URL(Link);
            HttpURLConnection connection=(HttpURLConnection)APIURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JSONArray jsonArray=new JSONArray(bufferedReader.readLine());
            bufferedReader.close();
            return jsonArray;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return  null;
        }
    }
    public static JSONObject sendSingleGetRequest(String Link){
        try{
            URL APIURL=new URL(Link);
            HttpURLConnection connection=(HttpURLConnection)APIURL.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JSONObject jsonObject=new JSONObject(bufferedReader.readLine());
            bufferedReader.close();
            return jsonObject;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return  null;
        }
    }
    public static String Encoder(JSONObject param){
        String data="";
        Iterator<String> keys =param.keys();

        try{
            while(keys.hasNext()) {
                String key = keys.next();
                data+=URLEncoder.encode(key+"","UTF-8");
                data+="=";
                data+=URLEncoder.encode(param.get(key)+"","UTF-8");
                data+="&";
            }
        }catch (Exception e){
            return  null;
        }
        return  data.substring(0,data.length()-1);
    }











     /*
    public static String prepareFormData(JSONArray str_json){
        String data_encoded="";
        try{
            for (int i=0;i<str_json.length();i++){
                JSONObject jsonObject=str_json.getJSONObject(i);
                String key=jsonObject.getString("key");
                String value=jsonObject.getString("value");
                data_encoded+=URLEncoder.encode(""+key,"UTF-8");
                data_encoded+="="+URLEncoder.encode(""+value,"UTF-8");
            }
            System.out.println("DATA ENCODED IS ******"+data_encoded);
            return  data_encoded;
        }catch (Exception e){
            return  data_encoded;
        }
    }
    */
    /*
    public static void Response(InputStream inputStream){
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuffer response = new StringBuffer();
       try{
           while((line = rd.readLine()) != null) {
               response.append(line);
               response.append('\r');
           }
           System.out.println(rd.readLine());
           rd.close();
           System.out.println("=========hatem=======>"+response.toString());
       }catch (Exception e){
           System.out.println("*********Error********** "+e.getMessage());
       }

    }
    */











}

