/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rpc;

import org.json.simple.JSONObject;

/**
 *
 * @author gary
 */
public class JSonBuilder {
    
    public JSONObject getSessionKey(String username, String password, Integer id){
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();
        json.put("method","get_session_key");
        params.put("username", username);
        params.put("password", password);
        json.put("params", params);
        json.put("id", id);
        return json;
    }

    public JSONObject listUsers(String sessionKey, Integer id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();
        json.put("method","list_users");
        params.put("sSessionKey", sessionKey);
        json.put("params", params);
        json.put("id", id);
        return json;
    }
    
    public JSONObject releaseSessionKey(String sessionKey, Integer id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();
        json.put("method","release_session_key");
        params.put("sSessionKey", sessionKey);
        json.put("params", params);
        json.put("id", id);
        return json;
    }

    public JSONObject listGroups(String sessionKey, Integer idSurvey, Integer id){
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();
        json.put("method","list_groups");
        params.put("sSessionKey", sessionKey);
        params.put("iSurveyID", idSurvey);
        json.put("params", params);
        json.put("id", id);
        return json;
    }

    JSONObject addSurvey(String sessionKey, Integer idSurvey, String surveyTitle, String surveyLanguage, String sFormat, Integer id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();
        json.put("method","add_survey");
        params.put("sSessionKey", sessionKey);
        params.put("iSurveyID", idSurvey);
        params.put("sSurveyTitle", surveyTitle);
        params.put("sSurveyLanguage", surveyLanguage);
        params.put("sformat", sFormat);
        json.put("params", params);
        json.put("id", id);
        return json;
    }

    public JSONObject listSurveys(String sessionKey, String user, Integer id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();
        json.put("method","list_surveys");
        params.put("sSessionKey", sessionKey);
        params.put("sUser", user);
        json.put("params", params);
        json.put("id", id);
        return json;
    }

    JSONObject getSummary(String sessionKey, Integer iSurveyId, String statName, Integer id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();
        json.put("method","get_summary");
        params.put("sSessionKey", sessionKey);
        params.put("iSurveyID", iSurveyId);
        params.put("sStatname", statName);
        json.put("params", params);
        json.put("id", id);
        return json;
    }
}
