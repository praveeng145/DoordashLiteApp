package com.ddlite.android.presenter;

public class BasePresenter {

    public enum PresenterState {
        NETWORK_REQUEST_START,
        NETWORK_REQUEST_END,
        NETWORK_ERROR,
        ITEM_CLICK
    }

    protected final static String SCHEME = "https";
    protected final static String AUTHORITY = "api.doordash.com";
    protected final static String PATH = "v2/restaurant/";

}
