package com.techvedika.breaq.constant;

/**
 * Created by Ibrar on 8/1/2018.
 */

public class Constants {

    //http://192.168.0.104:8089/bq_user
    //http://192.168.0.104:8090/bq_franchise

    private static final String URL = "http://192.168.0.104:8089/";

    private static final String URL_TWO = "http://192.168.0.104:8090/";


    public static final String kLogin_url = URL+"bq_user/user/signIn";
    public static final String kSignup_url = URL+"bq_user/user/signUp";
    public static final String kGet_franchise_type_url = URL+"bq_user/user/getFranchiseTypes";
    public static final String kGet_Franchise_url = URL+"bq_user/user/getFranchises";

    public static final String kAdd_Product_url = URL_TWO+"bq_franchise/franchise/addProduct";


    public static final String kSession_Creation_url = URL_TWO+"bq_franchise/franchise/createSession";

    public static final String kLogout_Url = "";
    public static final String kUpdateMessagingToken_Url="";

    public static boolean isLogEnable = true;

    // Response code

    public static final String kSuccessCode = "200";

    // Response Keys
    public static final String kStatus_Code = "statusCode";
    public static final String kStatusMessage = "statusMessage";
    public static final String kServer_Token = "token";
    public static final String kSession_key = "session_key";
    public static final String kSuccess = "success";
    public static final String kSuccess_True = "true";
    public static final String kSuccess_False = "false";
    public static final String kErrors = "errors";
    public static final String kData = "data";

    // Request Keys

    public static final String kEmail = "email";
    public static final String kPassword = "password";
    public static final String kFirstName = "first_name";
    public static final String kLastName = "last_name";
    public static final String kPhoneNumber ="phone_number";
    public static final String kCityName = "city";
    public static final String kStateName = "state";
    public static final String kDevice_Token = "device_token";
    public static final String kDevice_Type = "device_type";
    public static final String kAndroid = "android";
    public static final String kApiVersion = "api-version";
    public static final String kApiCode = "1.0";

    public static final String kAuthorization = "authorization";
    public static final String kCookies = "cookie";
    public static final String kSession_Id = "sessionid=";

    // Error Keys
    public static class ErrorKeys {

        public static final String kDetail = "detail";
        public static final String kNonFieldErrors = "non_field_errors";
        public static final String kErrorCode = "errorCode";
        public static final String kErrorMessage = "errorMessage";
        public static final String kDataCouldNotRead = "The data couldn't be read because it isn't in correct format";
        public static final String kVerifyStatus = "verify_status";
        public static final String kEmailPasswordRequired = "Email and Password are required";
        public static final String kSomethingWentWrong = "Oops! Something went wrong";
        public static final String kApiVersionMismatchError = "api-version is not updated or not matching to latest api-version";
    }


    /**
     * The default socket timeout in milliseconds
     */
    public static final long DEFAULT_TIMEOUT_MS = 60000;

    /**
     * The default number of retries
     */
    public static final int DEFAULT_MAX_RETRIES = 0;

    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;
}
