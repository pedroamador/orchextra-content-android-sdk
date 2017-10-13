package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
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
 * retrieving a collection of all {@link ContentData}.
 */
public class GetSection extends UseCase<ContentData, GetSection.Params> {

  private final OcmRepository ocmRepository;
  private final CompositeDisposable disposables;

  @Singleton @Inject GetSection(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
    this.disposables = new CompositeDisposable();
  }

  @Override Observable<ContentData> buildUseCaseObservable(Params params) {
    Observable<ContentData> sectionElements =
        this.ocmRepository.getSectionElements(params.forceReload, params.section, params.imagesToDownload);

    sectionElements.subscribe(new Observer<ContentData>() {
      @Override public void onSubscribe(@NonNull Disposable d) {
        disposables.add(d);
      }

      @Override public void onNext(@NonNull ContentData contentData) {

      }

      @Override public void onError(@NonNull Throwable e) {

      }

      @Override public void onComplete() {
        disposables.clear();
      }
    });


    return sectionElements;
  }

  public static final class Params {

    private final boolean forceReload;
    private final String section;
    private final int imagesToDownload;

    private Params(boolean forceReload, String section, int imagesToDownload) {
      this.forceReload = forceReload;
      this.section = section;
      this.imagesToDownload = imagesToDownload;
    }

    public static Params forSection(boolean forceReload, String section, int imagestodownload) {
      return new Params(forceReload, section, imagestodownload);
    }
  }
}
