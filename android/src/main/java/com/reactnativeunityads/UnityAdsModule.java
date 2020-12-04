package com.reactnativeunityads;


import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import android.util.Log;

public class UnityAdsModule extends ReactContextBaseJavaModule {

  ReactApplicationContext reactContext;

  String unityGameID;
  String unityPlacementID;
  boolean testMode = true;
  boolean isReady = false;

  Promise showPromise;
  Promise loadPromise;

  UnityAdsModule(ReactApplicationContext context){
    super(context);
    reactContext = context;
  }

  private class UnityAdsListener implements IUnityAdsListener{
    @Override
    public void onUnityAdsReady(String placementId){
      isReady = true;
    }

    @Override
    public void onUnityAdsStart (String placementId){
      // Implement functionality for a user starting to watch an ad.
    }

    @Override
    public void onUnityAdsFinish (String placementId, UnityAds.FinishState finishState){

      try{
        if(showPromise != null)
        {
          showPromise.resolve(finishState.toString());
          isReady = false;
        }
      }catch(Exception ex){
        showPromise.resolve("ERROR");
        isReady = false;
      }
    }

    @Override
    public void onUnityAdsError (UnityAds.UnityAdsError error, String message){
      try{
        if(showPromise != null)
        {
          showPromise.reject("E_FAILED_TO_LOAD", message);
          isReady = false;
        }
      }catch(Exception ex){
        isReady = false;
      }
    }
  }

  @Override
  public String getName(){
    return "UnityAds";
  }

  // Example method
  // See https://facebook.github.io/react-native/docs/native-modules-android
  @ReactMethod
  public void multiply(int a, int b, Promise promise){
    promise.resolve(a * b);
  }

  @ReactMethod
  public void initialized(String gameId, String placementId, Boolean test){
    testMode = test;
    unityGameID = gameId;
    unityPlacementID = placementId;

    final UnityAdsListener adListener = new UnityAdsListener();
    UnityAds.addListener(adListener);
    UnityAds.initialize(reactContext.getApplicationContext(), unityGameID, testMode);
  }

  @ReactMethod
  public void isLoad(Promise p){
    p.resolve(isReady);
  }

  @ReactMethod
  public void showAd(Promise p){
    showPromise = p;

    if(UnityAds.isReady(unityPlacementID)){
      UnityAds.show(reactContext.getCurrentActivity(), unityPlacementID);
    }else{
      showPromise.resolve("NOT_LOADED");
      showPromise = null;
    }
  }
}
