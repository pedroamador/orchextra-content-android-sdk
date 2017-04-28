package com.gigigo.orchextra.core.domain.interactors.home;

/*
@RunWith(MockitoJUnitRunner.class)
public class RetrieveSectionUiMenuInteractorTest {

  @Mock ConnectionUtils connectionUtils;

  @Mock MenuNetworkDataSource menuNetworkDataSource;

  @Mock DataBaseDataSource dataBaseDataSource;

  private GetMenuDataInteractor interactor;

  @Before public void setUp() throws Exception {
    MenuNetworkDomainService menuNetworkDomainService = new MenuNetworkDomainService(connectionUtils, menuNetworkDataSource);
    interactor = new GetMenuDataInteractor(dataBaseDataSource, menuNetworkDomainService);
  }

  @Test public void shouldReturnNoNetworkConnectionErrorWhenDeviceNoHasInternetConnection()
      throws Exception {

    when(connectionUtils.hasConnection()).thenReturn(false);

    InteractorResponse<MenuContentData> response = interactor.call();

    assertThat(response.hasError()).isTrue();
    assertThat(response.getError()).isExactlyInstanceOf(NoNetworkConnectionError.class);
  }

  @Test public void shouldReturnValueListWhenHasConnectionAndRequestWasSuccessful() throws Exception {
    BusinessObject<MenuContentData> fakeSuccessfulBoMenuContentData = getFakeSuccessfulMenuContentData();

    when(connectionUtils.hasConnection()).thenReturn(true);
    when(menuNetworkDataSource.getMenuContentData()).thenReturn(fakeSuccessfulBoMenuContentData);

    InteractorResponse<MenuContentData> response = interactor.call();

    assertThat(response.hasError()).isFalse();
    assertThat(response.getResult()).isEqualTo(fakeSuccessfulBoMenuContentData.getData());
  }

  @Test public void shouldReturnGenericInteractorErrorWhenRequestFailed() throws Exception {
    BusinessObject<MenuContentData> fakeFailedBoMenuContentData = getFakeFailedMenuContentData();

    when(connectionUtils.hasConnection()).thenReturn(true);
    when(menuNetworkDataSource.getMenuContentData()).thenReturn(fakeFailedBoMenuContentData);

    InteractorResponse<MenuContentData> response = interactor.call();

    assertThat(response.hasError()).isTrue();
    assertThat(response.getError()).isExactlyInstanceOf(GenericResponseDataError.class);
  }

  private BusinessObject<MenuContentData> getFakeSuccessfulMenuContentData() {
    MenuContentData menuContentData = new MenuContentData();

    return new BusinessObject<>(menuContentData, BusinessError.createOKInstance());
  }

  private BusinessObject<MenuContentData> getFakeFailedMenuContentData() {
    return new BusinessObject<>(null, BusinessError.createKOInstance(""));
  }
}*/