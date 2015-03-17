/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Add Solutions
 */
public class RpcConn{
    
    private String sessionKey;
    
    public String getSessionKey(){
        return this.sessionKey;
    }

    public Object request (JSONObject jsonRequest) throws UnsupportedEncodingException, IOException, ParseException{
                DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://isurvey-lime.pgagroup.co/index.php/admin/remotecontrol");
        post.setHeader("Content-type", "application/json");
        post.setEntity( new StringEntity(jsonRequest.toString()));
        System.out.println("jsonRequest: "+jsonRequest.toString());
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                JSONParser parser=new JSONParser();
                HttpEntity entity = response.getEntity();
                Object jsonResponse = parser.parse(EntityUtils.toString(entity));
                //JSONArray array=(JSONArray)obj;
                System.out.println("jsonResponse: " + jsonResponse.toString());
                return jsonResponse;
            }
        }catch(IOException e){
                e.printStackTrace();
        }
        return null;
    }

    public String getSessionKeyRPC(String username, String password, Integer id) throws IOException, UnsupportedEncodingException, ParseException{
        JSonBuilder builder = new JSonBuilder();
        JSonResults res = new JSonResults();
        JSONObject objeto = builder.getSessionKey(username, password, id);
        Object obj = request(objeto);
        this.sessionKey = res.stringResult(obj);
        System.out.println("sessionKey = " + sessionKey);
        return sessionKey;
    }
    
    public Object listUsers(String sessionKey, Integer id) throws IOException, UnsupportedEncodingException, ParseException{
        JSonBuilder builder = new JSonBuilder();
        JSonResults res = new JSonResults();
        JSONObject objeto = builder.listUsers(sessionKey, id);
        Object obj = request(objeto);
        System.out.println("listUsers = " + res.objectResult(obj));
        return res.objectResult(obj);
    }
    
    public void releaseSessionKey(String sessionKey, Integer id) throws IOException, UnsupportedEncodingException, ParseException{
        JSonBuilder builder = new JSonBuilder();
        JSonResults res = new JSonResults();
        JSONObject objeto = builder.releaseSessionKey(sessionKey, id);
        Object obj = request(objeto);
        System.out.println("releaseSessionKey = " + res.stringResult(obj));
    }

    public Object listGroups(String sessionKey, Integer idSurvey, Integer id) throws IOException, UnsupportedEncodingException, ParseException{
        JSonBuilder builder = new JSonBuilder();
        JSonResults res = new JSonResults();
        JSONObject objeto = builder.listGroups(sessionKey, idSurvey, id);
        Object obj = request(objeto);
        System.out.println("listGroups = " + res.objectResult(obj));
        return res.objectResult(obj);
    }

    public Object addSurvey(String sessionKey, Integer idSurvey, String surveyTitle, String surveyLanguage, String sFormat, Integer id) throws UnsupportedEncodingException, IOException, ParseException{
        JSonBuilder builder = new JSonBuilder();
        JSonResults res = new JSonResults();
        JSONObject objeto = builder.addSurvey(sessionKey, idSurvey, surveyTitle, surveyLanguage, sFormat, id);
        Object obj = request(objeto);
        System.out.println("addSurvey = " + res.objectResult(obj));
        return res.objectResult(obj);
    }

    public Object listSurveys(String sessionKey, String user, Integer id) throws IOException, UnsupportedEncodingException, ParseException{
        JSonBuilder builder = new JSonBuilder();
        JSonResults res = new JSonResults();
        JSONObject objeto = builder.listSurveys(sessionKey, user, id);
        Object obj = request(objeto);
        System.out.println("listSurveys = " + res.objectResult(obj));
        return res.objectResult(obj);
    }

    public Object getSummary(String sessionKey, Integer iSurveyId, String statName, Integer id) throws IOException, UnsupportedEncodingException, ParseException{
        JSonBuilder builder = new JSonBuilder();
        JSonResults res = new JSonResults();
        JSONObject objeto = builder.getSummary(sessionKey, iSurveyId, statName, id);
        Object obj = request(objeto);
        System.out.println("listSurveys = " + res.objectResult(obj));
        return res.objectResult(obj);
    }
}
