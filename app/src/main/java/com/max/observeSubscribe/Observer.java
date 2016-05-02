package com.max.observeSubscribe;



/**
 * Created by Max on 5/2/2016.
 */
public interface Observer<T> {

    //method to update the observer, used by subject
    public void update(T update);

    //attach with subject to observe
    public void setSubject(Subject<T> sub);
}
