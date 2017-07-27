package com.gigigo.orchextra.core.data.rxExecutor;

import io.reactivex.Observable;

/**
 * Created by francisco.hernandez on 27/7/17.
 */

public abstract class PriorityObservable<T> extends Observable<T> implements PriorityWorker  {
}
