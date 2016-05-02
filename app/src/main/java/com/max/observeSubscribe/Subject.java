package com.max.observeSubscribe;

/**
 * Created by Max on 5/2/2016.
 */
public interface Subject<T> {

    //methods to register and unregister observers
    public void register(Observer<T> obj);
    public void unregister(Observer<T> obj);
}