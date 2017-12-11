package com.gigigo.orchextra.core.sdk.di.modules;

import android.app.Application;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.sdk.OcmSchemeHandler;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.OcmStyleUiImp;
import com.gigigo.orchextra.core.sdk.OcmViewGeneratorImp;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.application.OcmContextProviderImpl;
import com.gigigo.orchextra.core.sdk.application.OcmSdkLifecycle;
import com.gigigo.orchextra.wrapper.Ox3ManagerImp;
import com.gigigo.orchextra.wrapper.OxManager;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Provider;
import orchextra.javax.inject.Singleton;

@Module(includes = { ControllerModule.class, PresentationModule.class }) public class OcmModule {

  private final Application app;

  public OcmModule(Application app) {
    this.app = app;
  }

  @Singleton @Provides OcmContextProvider provideOcmContextProvider() {
    return new OcmContextProviderImpl(app.getApplicationContext());
  }

  @Singleton @Provides OcmSdkLifecycle provideOcmSdkLifecycle(OcmContextProvider ocmContextProvider,
      PriorityScheduler priorityScheduler) {
    OcmSdkLifecycle ocmSdkLifecycle = new OcmSdkLifecycle(priorityScheduler);

    ocmContextProvider.setOcmActivityLifecycle(ocmSdkLifecycle);

    return ocmSdkLifecycle;
  }

  @Singleton @Provides OcmViewGenerator provideOcmViewGenerator(OcmController ocmController,
      Provider<DetailElementsViewPresenter> detailElementsViewPresenterProvides) {
    return new OcmViewGeneratorImp(ocmController, detailElementsViewPresenterProvides);
  }

  @Singleton @Provides Authoritation provideAuthoritation() {
    return new Authoritation();
  }

  @Singleton @Provides ActionHandler provideActionHandler(OcmContextProvider ocmContextProvider) {
    return new ActionHandler(ocmContextProvider);
  }

  @Singleton @Provides OcmSchemeHandler provideOcmSchemeHandler(OcmContextProvider contextProvider,
      OcmController ocmController, ActionHandler actionHandler, Authoritation authoritation) {
    return new OcmSchemeHandler(contextProvider, ocmController, actionHandler, authoritation);
  }

  @Singleton @Provides OcmStyleUi provideOcmStyleUi() {
    return new OcmStyleUiImp();
  }

  @Singleton @Provides OxManager provideOxManager() {
    return new Ox3ManagerImp();
  }
}
