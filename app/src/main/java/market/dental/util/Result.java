package market.dental.util;

import com.google.gson.annotations.Expose;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 *
 * @author Kemal Sami KARACA
 */
public class Result <T> implements Serializable{

    public static Result SUCCESS;
    public static Result SUCCESS_EMPTY;
    public static Result SUCCESS_LOGOUT;
    public static Result FAILURE_AUTH;
    public static Result FAILURE_AUTH_SESSION;
    public static Result FAILURE_AUTH_WRONG;
    public static Result FAILURE_AUTH_MULTIPLE;
    public static Result FAILURE_AUTH_PERMISSION;
    public static Result FAILURE_AUTH_REGISTER;
    public static Result FAILURE_DB;
    public static Result FAILURE_DB_UPDATE;
    public static Result FAILURE_DB_EFFECTED_ROW_NUM;
    public static Result FAILURE_DB_PRIMARY_KEY;
    public static Result FAILURE_CACHE;
    public static Result FAILURE_PARAM_WRONG;
    public static Result FAILURE_PARAM_MISMATCH;
    public static Result FAILURE_PARAM_INVALID;
    public static Result FAILURE_PROCESS;
    public static Result FAILURE_PROCESS_CASTING;
    public static Result FAILURE_PROCESS_CONTENTTYPE;
    public static Result FAILURE_CHECKER_DATE;
    public static Result FAILURE_USER_CONFIRM;
    public static Result LOG_TAG_INFO;

    static{
        //initializeStaticObjects(ResourceBundle.getBundle("market.dental.resources.resultProp", Locale.ENGLISH));
        initializeStaticObjects();
    }

    @Expose
    private String resultCode;
    @Expose
    private String resultText;
    @Expose
    private Object parameter;
    @Expose
    private T content;

    public Result(){}

    public Result(JSONObject jsonObject){
        try {
            this.resultText = jsonObject.getString("resultText");
            this.resultCode = jsonObject.getString("resultCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Result(String resultCode, String resultText){
        this.resultCode = resultCode;
        this.resultText = resultText;
    }


    /**
     * This method checks whether the resultCode of the object equals to the resultCode of the given result object
     *
     * @param result The result parameter to be compared with the object
     * @return Returns true if given result object matches the current one
     */
    public boolean checkResult(Result result){
        if(result!=null && this.getResultCode().equals(result.getResultCode()))
            return true;
        else
            return false;
    }
    /**
     * This method is used to set a content into the Result object.
     * One important thing to consider is that this method creates a new instance of Result class.
     * So calling this method after calling setParameter() method will erase the information that you set in setParameter() method.
     * You should use setContentAndParameter() method instead.
     *
     * @param content the object to be set which is an instance of given generic class T
     * @return Returns the result object with the content set
     */
    public Result<T> setContent(T content) {
        Result<T> returnResult = new Result<T>(this.resultCode, this.resultText);
        returnResult.content = content;
        return returnResult;
    }
    /**
     * This method is used to set a custom parameter or message into the Result object.
     * One important thing to consider is that this method creates a new instance of Result class.
     * So calling this method after calling setContent() method will erase the information that you set in setContent() method.
     * You should use setContentAndParameter() method instead.
     *
     * @param parameter the parameter to be set which can be a custom message or another object
     * @return Returns the result object with the parameter set
     */
    public Result<T> setParameter(Object parameter) {
        Result<T> returnResult = new Result<T>(this.resultCode, this.resultText);
        returnResult.parameter = parameter;
        return returnResult;
    }
    /**
     * This method is used to set a content and a custom parameter or message into the Result object.
     * One important thing to consider is that this method creates a new instance of Result class.
     * So calling this method after calling setContent() method will erase the information that you set in setContent() method.
     * You should use setContentAndParameter() method instead.
     *
     * @param content the object to be set which is an instance of given generic class T
     * @param parameter the parameter to be set which can be a custom message or another object
     * @return Returns the result object with the content and the parameter set
     */
    public Result<T> setContentAndParameter(T content, Object parameter){
        Result<T> returnResult = new Result<T>(this.resultCode, this.resultText);
        returnResult.content = content;
        returnResult.parameter = parameter;
        return returnResult;
    }
    /**
     * This method is used to set language of the messages that Result objects contain.
     * One call for this method is enough to change all of the messages to given language.
     * Default language parameter for a Result object is English. This language can be changed by using this method.
     *
     * @param lang Language parameter that changes the message language. Default is 'en'. For Turkish, should be set as 'tr'
     */
    public static void setLanguage(String lang){
        String propFileName = lang==null ? "resources_en" : lang.equals("tr") ? "resources_tr" : "resources_en";
        initializeStaticObjects(ResourceBundle.getBundle("market.dental.resources.resultProp", lang==null ? Locale.ENGLISH : lang.equals("tr") ? new Locale("tr") : Locale.ENGLISH));
    }
    /**
     * This method initializes the static Result object with the given language via Resource Bundle
     *
     * @param rs Resource Bundle that contains a properties file with desired language
     */
    private static void initializeStaticObjects(ResourceBundle rs){
        SUCCESS                     = new Result("GUPPY.001", rs.getString("result.guppy.001"));
        SUCCESS_EMPTY               = new Result("GUPPY.010", rs.getString("result.guppy.010"));
        SUCCESS_LOGOUT              = new Result("GUPPY.090", rs.getString("result.guppy.090"));
        FAILURE_AUTH                = new Result("GUPPY.101", rs.getString("result.guppy.101"));
        FAILURE_AUTH_SESSION        = new Result("GUPPY.102", rs.getString("result.guppy.102"));
        FAILURE_AUTH_WRONG          = new Result("GUPPY.111", rs.getString("result.guppy.111"));
        FAILURE_AUTH_MULTIPLE       = new Result("GUPPY.121", rs.getString("result.guppy.121"));
        FAILURE_AUTH_PERMISSION     = new Result("GUPPY.131", rs.getString("result.guppy.131"));
        FAILURE_AUTH_REGISTER       = new Result("GUPPY.151", rs.getString("result.guppy.151"));
        FAILURE_DB                  = new Result("GUPPY.201", rs.getString("result.guppy.201"));
        FAILURE_DB_UPDATE           = new Result("GUPPY.221", rs.getString("result.guppy.221"));
        FAILURE_DB_EFFECTED_ROW_NUM = new Result("GUPPY.241", rs.getString("result.guppy.241"));
        FAILURE_DB_PRIMARY_KEY      = new Result("GUPPY.251", rs.getString("result.guppy.251"));
        FAILURE_CACHE               = new Result("GUPPY.301", rs.getString("result.guppy.301"));
        FAILURE_PARAM_WRONG         = new Result("GUPPY.501", rs.getString("result.guppy.501"));
        FAILURE_PARAM_MISMATCH      = new Result("GUPPY.511", rs.getString("result.guppy.511"));
        FAILURE_PARAM_INVALID       = new Result("GUPPY.512", rs.getString("result.guppy.512"));
        FAILURE_PROCESS             = new Result("GUPPY.600", rs.getString("result.guppy.600"));
        FAILURE_PROCESS_CASTING     = new Result("GUPPY.610", rs.getString("result.guppy.610"));
        FAILURE_PROCESS_CONTENTTYPE = new Result("GUPPY.611", rs.getString("result.guppy.611"));
        FAILURE_CHECKER_DATE        = new Result("GUPPY.740", rs.getString("result.guppy.740"));
        LOG_TAG_INFO                = new Result("TAG.001", rs.getString("result.tag.001"));
    }

    private static void initializeStaticObjects(){
        SUCCESS                     = new Result("GUPPY.001", "SUCCESS");
        SUCCESS_EMPTY               = new Result("GUPPY.010", "SUCCESS_EMPTY");
        SUCCESS_LOGOUT              = new Result("GUPPY.090", "SUCCESS_LOGOUT");
        FAILURE_AUTH                = new Result("GUPPY.101", "FAILURE_AUTH");
        FAILURE_AUTH_SESSION        = new Result("GUPPY.102", "FAILURE_AUTH_SESSION");
        FAILURE_AUTH_WRONG          = new Result("GUPPY.111", "FAILURE_AUTH_WRONG");
        FAILURE_AUTH_MULTIPLE       = new Result("GUPPY.121", "FAILURE_AUTH_MULTIPLE");
        FAILURE_AUTH_PERMISSION     = new Result("GUPPY.131", "FAILURE_AUTH_PERMISSION");
        FAILURE_AUTH_REGISTER       = new Result("GUPPY.151", "FAILURE_AUTH_REGISTER");
        FAILURE_DB                  = new Result("GUPPY.201", "FAILURE_DB");
        FAILURE_DB_UPDATE           = new Result("GUPPY.221", "FAILURE_DB_UPDATE");
        FAILURE_DB_EFFECTED_ROW_NUM = new Result("GUPPY.241", "FAILURE_DB_EFFECTED_ROW_NUM");
        FAILURE_DB_PRIMARY_KEY      = new Result("GUPPY.251", "FAILURE_DB_PRIMARY_KEY");
        FAILURE_CACHE               = new Result("GUPPY.301", "FAILURE_CACHE");
        FAILURE_PARAM_WRONG         = new Result("GUPPY.501", "FAILURE_PARAM_WRONG");
        FAILURE_PARAM_MISMATCH      = new Result("GUPPY.511", "FAILURE_PARAM_MISMATCH");
        FAILURE_PARAM_INVALID       = new Result("GUPPY.512", "FAILURE_PARAM_INVALID");
        FAILURE_PROCESS             = new Result("GUPPY.600", "FAILURE_PROCESS");
        FAILURE_PROCESS_CASTING     = new Result("GUPPY.610", "FAILURE_PROCESS_CASTING");
        FAILURE_PROCESS_CONTENTTYPE = new Result("GUPPY.611", "FAILURE_PROCESS_CONTENTTYPE");
        FAILURE_CHECKER_DATE        = new Result("GUPPY.740", "FAILURE_CHECKER_DATE");
        FAILURE_USER_CONFIRM        = new Result("GUPPY.702", "User not confirmed.");               // dental.market standartına göre eklendi
        LOG_TAG_INFO                = new Result("TAG.001", "INFO_DENTAL_MARKET");
    }

    /*Default Getters-Setters*/
    public String getResultCode() {
        return resultCode;
    }
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    public String getResultText() {
        return resultText;
    }
    public void setResultText(String resultText) {
        this.resultText = resultText;
    }
    public Object getParameter() {
        return parameter;
    }
    public T getContent() {
        return content;
    }

}