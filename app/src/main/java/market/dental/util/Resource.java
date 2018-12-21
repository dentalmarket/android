package market.dental.util;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by kemalsamikaraca on 15.01.2018.
 */

public class Resource {

    public static String ajax_login = "https://dental.market/api/set/login";
    public static String ajax_register = "https://dental.market/api/set/register";
    public static String ajax_profile_update = "https://dental.market/api/set/updateProfile";
    public static String ajax_update_device_token = "https://dental.market/api/set/updateDeviceToken";
    public static String ajax_logout = "https://dental.market/api/set/logout";
    public static String ajax_forgot_password = "https://dental.market/api/set/forgotPassword";
    public static String ajax_get_banner_images_url = "https://dental.market/api/get/banners";
    public static String ajax_get_categories = "https://dental.market/api/get/categories";
    public static String ajax_get_products_homeproduct_url = "https://dental.market/api/get/homeProducts";
    public static String ajax_get_products_by_category = "https://dental.market/api/get/productsByCategory";
    public static String ajax_get_product_detail_url = "https://dental.market/api/get/product";
    public static String ajax_get_product_by_search_key = "https://dental.market/api/get/searchProducts";
    public static String ajax_get_products_by_user_history = "https://dental.market/api/get/userHistory";
    public static String ajax_get_my_offer_requests = "https://dental.market/api/get/myOfferRequests";
    public static String ajax_get_offer_request_list = "https://dental.market/api/get/offerRequests";
    public static String ajax_get_offer_request = "https://dental.market/api/get/offerRequest";
    public static String ajax_set_offer_request_with_id = "https://dental.market/api/set/offerRequestWithId";
    public static String ajax_set_offer_request = "https://dental.market/api/set/offerRequest";
    public static String ajax_set_offer = "https://dental.market/api/set/offer";
    public static String ajax_get_productsautocomplete = "https://dental.market/api/get/productsAutoComplete";
    public static String ajax_get_professions = "https://dental.market/api/get/jobs";
    public static String ajax_get_city_list = "https://dental.market/api/get/cities";
    public static String ajax_get_borough_list = "https://dental.market/api/get/towns";
    public static String ajax_get_conversation_list = "https://dental.market/api/get/userMessages";
    public static String ajax_get_message_list = "https://dental.market/api/get/convoMessages";
    public static String ajax_send_message = "https://dental.market/api/set/sendMessage";

    public static String DOMAIN_NAME = "https://dental.market";
    public static String SHAREDPREF_SEARCH_KEY = "SHAREDPREF_SEARCH_KEY";
    public static String STATIC_ANDROID_API_TOKEN = "izcfpXu74i6YgIxSG712AEaohc0FfYpC";
    public static String VALUE_API_TOKEN = "";
    public static String KEY_API_TOKEN = "api_token";
    public static String KEY_LOCAL_BROADCAST = "KEY_LOCAL_BROADCAST";
    public static String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";
    public static String KEY_PRODUCT_DESC = "KEY_PRODUCT_DESC";
    public static String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";
    public static String KEY_MESSAGE_RECEIVER_ID = "KEY_MESSAGE_RECEIVER_ID";
    public static String KEY_MESSAGE_CATEGORY_ID = "KEY_MESSAGE_CATEGORY_ID";
    public static String KEY_CONVERSATION_ID = "KEY_CONVERSATION_ID";
    public static String KEY_GET_RECENT_PRODUCTS = "KEY_GET_RECENT_PRODUCTS";
    public static String KEY_FRAGMENT_TITLE = "KEY_FRAGMENT_TITLE";

    public static void setDefaultAPITOKEN(){
        VALUE_API_TOKEN = STATIC_ANDROID_API_TOKEN;
    }
}
