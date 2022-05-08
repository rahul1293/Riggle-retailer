package com.riggle.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.riggle.data.models.ApiError;
import com.riggle.services.eventbus.EventBusEvents;
import com.riggle.services.eventbus.GlobalBus;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Developer: Anshul Patro
 */
public final class ErrorUtils {

    public static final int DEFAULT_STATUS_CODE = 600;

    /**
     * Parses error from the api response
     *
     * @param mRetrofit the m retrofit
     * @param response  the api response
     * @return parsed instance of ApiError
     */
    public static ApiError parseError(final Retrofit mRetrofit, final Response<?> response) {
//        Converter<ResponseBody, ApiError> converter = mRetrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;
        try {
//            if (response.errorBody() != null) {
//                MyloLogger.e("API_error", "boby::"+ (new Gson()).toJson(response.body()));
//                error = converter.convert(response.errorBody());
//
//            } else {
//            MyloLogger.e("error:body", response.errorBody().string());
            JSONObject jsonObject = null;
            if (response.errorBody() != null) {
                jsonObject = new JSONObject(response.errorBody().string());
            }
            if (!jsonObject.getString("message").isEmpty()) {
                error = new ApiError(response.code(), jsonObject.getString("message"), response.isSuccessful(), response.errorBody().string(), jsonObject.getString("message"));
            } else {
                error = new ApiError(response.code(), response.message(), response.isSuccessful(), response.errorBody().string(), response.message());
            }
//            }
        } catch (final Exception e) {
            e.printStackTrace();
            int statusCode = DEFAULT_STATUS_CODE;
            // keeping empty string as we cannot reference direct strings here
            String message = "Something when wrong.";
            if (response.code() != 0) {
                statusCode = response.code();
            }
            if (response.message() != null && !response.message().isEmpty()) {
                message = response.message();
            }
            error = new ApiError(statusCode, message, response.isSuccessful(), ExceptionUtil.convertStackTraceToString(e.getStackTrace()), message);
        }
        return error;
    }

    /**
     * method to handle the error while network call
     *
     * @param cause
     * @return
     */
    public static ApiError resolveNetworkError(Throwable cause) {
        ApiError error = new ApiError();
        error.setSuccess(false);
        if (cause instanceof UnknownHostException) {
            error.setMessage("No internet connection. Please try again later.");
            error.setStatusCode(601);
        } else if (cause instanceof SocketTimeoutException) {
            error.setMessage("Application server could not respond. Please try later.");
            error.setStatusCode(602);
        } else if (cause instanceof ConnectException) {
            error.setMessage("No internet connection. Please try again later.");
            error.setStatusCode(603);
        } else if (cause instanceof JsonSyntaxException) {
            cause.printStackTrace();
            error.setMessage("Parsing error");
            error.setStatusCode(604);
        } else {
            error.setMessage("" + cause.getLocalizedMessage());
//            error.setMessage("Something went wrong. Please try again later");
            error.setStatusCode(DEFAULT_STATUS_CODE);
        }
        error.setBody(ExceptionUtil.convertStackTraceToString(cause.getStackTrace()));
        return error;
    }

    /**
     * method to handle all the error while making any api call
     *
     * @param error
     */
    public static void handleAPIError(ApiError error, String apiMethod) {
        if (error.getStatusCode() == 401) {
            EventBusEvents.LogoutUser logoutUser = new EventBusEvents.LogoutUser(true);
            GlobalBus.getBus().post(logoutUser);
        }
    }


    /**
     * method to show the user error toast message based on the error code
     *
     * @param context
     * @param apiError
     */
    /*public static void showUserErrorMessage(Context context, ApiError apiError) {
        if (context == null)
            return;

        String message = context.getString(R.string.error_something_went_wrong);
        switch (apiError.getStatusCode()) {
            case 401:
                message = context.getString(R.string.login_expire);
                break;

            case 601:
            case 603:
                message = context.getString(R.string.error_no_internet_access);

        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }*/
}
