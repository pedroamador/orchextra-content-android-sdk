package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
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
 * retrieving a collection of all {@link ElementData}.
 */
public class GetDetail extends UseCase<ElementData, GetDetail.Params> {

  private final OcmRepository ocmRepository;
  private final CompositeDisposable disposables;

  @Singleton @Inject GetDetail(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
    this.disposables = new CompositeDisposable();
  }

  @Override Observable<ElementData> buildUseCaseObservable(Params params) {
    Observable<ElementData> detail =
        this.ocmRepository.getDetail(params.forceReload, params.content);

    detail.subscribe(new Observer<ElementData>() {
      @Override public void onSubscribe(@NonNull Disposable d) {
        disposables.add(d);
      }

      @Override public void onNext(@NonNull ElementData elementData) {

      }

      @Override public void onError(@NonNull Throwable e) {

      }

      @Override public void onComplete() {
        disposables.clear();
      }
    });

    return detail;
  }

  public static final class Params {

    private final boolean forceReload;
    private final String content;

    private Params(boolean forceReload, String content) {
      this.forceReload = forceReload;
      this.content = content;
    }

    public static Params forDetail(boolean forceReload, String content) {
      return new Params(forceReload, content);
    }
  }
}
