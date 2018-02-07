package com.gigigo.orchextra.core.controller

import android.util.Log
import com.fernandocejas.arrow.functions.Predicates.not
import com.gigigo.orchextra.core.controller.OcmControllerImpKt.Companion
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException
import com.gigigo.orchextra.core.data.rxException.ApiVersionNotFoundException
import com.gigigo.orchextra.core.domain.OcmControllerKt.GetVersionControllerCallback
import com.gigigo.orchextra.core.domain.OcmControllerKt
import com.gigigo.orchextra.core.domain.OcmControllerKt.GetMenusControllerCallback
import com.gigigo.orchextra.core.domain.entities.DataRequest.DEFAULT
import com.gigigo.orchextra.core.domain.entities.DataRequest.FORCE_CACHE
import com.gigigo.orchextra.core.domain.entities.DataRequest.FORCE_CLOUD
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import com.gigigo.orchextra.core.domain.rxInteractor.DefaultObserver
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection
import com.gigigo.orchextra.core.domain.rxInteractor.GetVersion
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler.Priority.HIGH
import com.gigigo.orchextra.core.sdk.utils.OcmPreferences
import com.gigigo.orchextra.ocm.dto.UiMenu
import com.gigigo.orchextra.ocm.dto.UiMenuData
import java.lang.Exception
import java.util.ArrayList

class OcmControllerImpKt(
    val getVersion: GetVersion,
    val getMenus: GetMenus,
    val getSection: GetSection,
    val ocmPreferences: OcmPreferences) : OcmControllerKt {

  lateinit var menuCallback: GetMenusControllerCallback

  override fun updateContent(callback: GetMenusControllerCallback) {
    menuCallback = callback

    getVersion.execute(VersionObserver(object : GetVersionControllerCallback {
      override fun onVersionLoaded(versionData: VersionData) {
        checkVersion(versionData)
      }

      override fun onVersionFails(exception: Exception) {
        loadMenuSections()
      }

    }), GetVersion.Params.forVersion(), HIGH)
  }

  private fun checkVersion(versionData: VersionData) {
    if (versionHasChanged(versionData)) {
      ocmPreferences.saveVersion(versionData.version)
      loadMenuSections()
    } else {
      //DO NOTHING
      doNothing()
    }
  }

  private fun versionHasChanged(versionData: VersionData): Boolean =
      ocmPreferences.version != versionData.version


  private fun loadMenuSections() {
    getMenus.execute(MenuObserver(object : GetMenusControllerCallback {
      override fun onMenusLoaded(uiMenuData: UiMenuData?) {
        checkMenus(uiMenuData)
      }

      override fun onMenusFails(exception: Exception) {
        checkMenus()
      }

    }), GetMenus.Params.forForceSource(FORCE_CACHE), HIGH)
  }

  private fun checkMenus(cachedMenuData: UiMenuData? = null) {
    getMenus.execute(MenuObserver(object : GetMenusControllerCallback {
      override fun onMenusLoaded(uiMenuData: UiMenuData?) {
        if (menusHasChanged(cachedMenuData,uiMenuData)) {
          //DISPLAY NEW CONTENT AVAILABLE BUTTON
          displayNewContentAvailableButton(uiMenuData)
        } else {
          loadSections(uiMenuData)
        }
      }

      override fun onMenusFails(exception: Exception) {
        //DISPLAY NEW CONTENT AVAILABLE BUTTON
        displayNewContentAvailableButton(cachedMenuData)
      }

    }), GetMenus.Params.forForceSource(FORCE_CLOUD), HIGH)
  }

  private fun menusHasChanged(cachedMenuData: UiMenuData?, uiMenuData: UiMenuData?): Boolean {
    return uiMenuData?.uiMenuList?.equals(cachedMenuData?.uiMenuList) == false
  }


  private fun loadSections(uiMenuData: UiMenuData? = null) {
    menuCallback.onMenusLoaded(uiMenuData)
    // getSection.execute()

  }

  private fun checkSection() {
    if (sectionUpdated()) {
      //DISPLAY NEW CONTENT AVAILABLE BUTTON
      displayNewContentAvailableButton()
    } else {
      //DO NOTHING
      doNothing()
    }
  }

  private fun sectionUpdated(): Boolean =
      true


  private fun displayNewContentAvailableButton(uiMenuData: UiMenuData? = null) {
    menuCallback.onMenusLoaded(uiMenuData)
  }

  private fun doNothing(uiMenuData: UiMenuData? = null) {
    menuCallback.onMenusLoaded(uiMenuData)
  }

  companion object {
    fun transformMenu(menuContentData: MenuContentData?): UiMenuData {

      val time = System.currentTimeMillis()

      val uiMenuData = UiMenuData()

      val menuList = ArrayList<UiMenu>()

      if (menuContentData != null
          && menuContentData.menuContentList != null
          && menuContentData.menuContentList.size > 0) {

        uiMenuData.isFromCloud = menuContentData.isFromCloud

        for (element in menuContentData.menuContentList[0].elements) {
          val uiMenu = UiMenu()

          uiMenu.slug = element.slug
          uiMenu.text = element.sectionView!!.text
          uiMenu.elementUrl = element.elementUrl

          if (menuContentData.elementsCache != null) {
            val elementCache = menuContentData.elementsCache[element.elementUrl]
            if (elementCache != null) {
              uiMenu.elementCache = elementCache
              uiMenu.updateAt = elementCache.updateAt
              if (elementCache.render != null) {
                uiMenu.contentUrl = elementCache.render.contentUrl
              }
            }
          }

          menuList.add(uiMenu)
        }
      }

      uiMenuData.uiMenuList = menuList

      Log.v("TT - UiMenuData", ((System.currentTimeMillis() - time) / 1000).toString() + "")

      return uiMenuData
    }
  }
}




class VersionObserver(
    private val getVersionControllerCallback: OcmControllerKt.GetVersionControllerCallback?) : DefaultObserver<VersionData>() {

  override fun onNext(versionData: VersionData) {
    getVersionControllerCallback?.onVersionLoaded(versionData)
  }

  override fun onError(e: Throwable) {
    getVersionControllerCallback?.onVersionFails(ApiVersionNotFoundException(e))
    e.printStackTrace()
  }
}

class MenuObserver(
    private val getMenusCallback: OcmControllerKt.GetMenusControllerCallback) : DefaultObserver<MenuContentData>() {

  override fun onComplete() {

  }

  override fun onNext(menuContentData: MenuContentData) {
    getMenusCallback.onMenusLoaded(OcmControllerImpKt.transformMenu(menuContentData))
  }

  override fun onError(e: Throwable) {
    getMenusCallback.onMenusFails(ApiMenuNotFoundException(e))
    e.printStackTrace()
  }
}

class SectionObserver(
    private val getSectionControllerCallback: OcmControllerKt.GetSectionControllerCallback?) : DefaultObserver<ContentData>() {

  override fun onComplete() {

  }

  override fun onNext(contentData: ContentData) {
    getSectionControllerCallback?.onSectionLoaded(contentData)
  }

  override fun onError(e: Throwable) {
    getSectionControllerCallback?.onSectionFails(ApiSectionNotFoundException(e))
    e.printStackTrace()
  }
}

