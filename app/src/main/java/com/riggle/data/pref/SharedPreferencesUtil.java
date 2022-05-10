package com.riggle.data.pref;

import com.riggle.data.models.response.UserData;
import com.riggle.data.models.response.UserDetails;

import java.util.ArrayList;

public interface SharedPreferencesUtil {

    void deleteAll();

    UserData getUserData();

    boolean saveUserData(UserData data);

    boolean saveRetailerDetails(UserDetails data);

    UserDetails getRetailerDetails();

    boolean isOrderPlaced();

    void saveRecentSearchesList(ArrayList<String> list);

    ArrayList<String> getRecentSearchesList();

    void setOrderPlaced(boolean value);

    void saveSupportNumber(String value);

    String getSupportNumber();

    void setCartCount(int count);

    Integer getCartCount();

}
