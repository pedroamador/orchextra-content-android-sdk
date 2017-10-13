package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
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
 * retrieving a collection of all {@link MenuContentData}.
 */
public class GetMenus extends UseCase<MenuContentData, GetMenus.Params> {

  private final OcmRepository ocmRepository;
  private final CompositeDisposable disposables;

  @Singleton @Inject GetMenus(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
    this.disposables = new CompositeDisposable();
  }

  @Override Observable<MenuContentData> buildUseCaseObservable(Params params) {
    Observable<MenuContentData> menu = this.ocmRepository.getMenu(params.forceReload);

    menu.subscribe(new Observer<MenuContentData>() {
      @Override public void onSubscribe(@NonNull Disposable d) {
        disposables.add(d);
      }

      @Override public void onNext(@NonNull MenuContentData menuContentData) {

      }

      @Override public void onError(@NonNull Throwable e) {

      }

      @Override public void onComplete() {
        disposables.clear();
      }
    });

    return menu;
  }

  public static final class Params {

    private final boolean forceReload;

    private Params(boolean forceReload) {
      this.forceReload = forceReload;
    }

    public static Params forForceReload(boolean forceReload) {
      return new Params(forceReload);
    }
  }
}
