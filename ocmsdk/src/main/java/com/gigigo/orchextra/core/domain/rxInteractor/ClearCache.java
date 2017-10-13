package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javax.inject.Singleton;
import orchextra.javax.inject.Inject;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * deleting stored data}.
 */
public class ClearCache extends UseCase<Void, ClearCache.Params> {

  private final OcmRepository ocmRepository;
  private final CompositeDisposable disposables;

    @Singleton @Inject ClearCache(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
    this.disposables = new CompositeDisposable();
  }

  @Override Observable<Void> buildUseCaseObservable(Params params) {
    Observable<Void> clear = this.ocmRepository.clear(params.images, params.data);

    clear.subscribe(new Observer<Void>() {
      @Override public void onSubscribe(@NonNull Disposable d) {
        disposables.add(d);
      }

      @Override public void onNext(@NonNull Void aVoid) {

      }

      @Override public void onError(@NonNull Throwable e) {

      }

      @Override public void onComplete() {
        disposables.clear();
      }
    });

    return clear;
  }

  public static final class Params {

    private final boolean images;
    private final boolean data;

    private Params(boolean images, boolean data) {
      this.images = images;
      this.data = data;
    }

    public static Params create(boolean images, boolean data) {
      return new Params(images, data);
    }
  }
}
