package com.demo.restservice;

/**
 * Created by root on 14/9/16.
 */

public interface OnApiResponseListener {
    public <E> void onSuccess(E t);
    public <E> void onError(E t);
    public  void onError();
}
