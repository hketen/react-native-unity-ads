import React, { useEffect } from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import UnityAds from 'react-native-unity-ads';

type FinishState = "ERROR" | "SKIPPED" | "COMPLETED" | "NOT_LOADED";

export default function App() {

  useEffect(() => {
    console.log("load challenge");
    UnityAds.loadAd('3873165', 'video', __DEV__).then(value=>{
      console.log(value);
    }).catch(error=>{
      console.log(error);
    });
  }, []);

  const showAd = async () => {
    console.log(await UnityAds.isLoad());

    UnityAds.isLoad().then(isLoad=>{
      if(isLoad){
        UnityAds.showAd().then((result)=>{
          console.log(result);
        }).catch(error=>{
          console.log(error);
        });
      }
    });
  }

  return (
    <View style={styles.container}>
      <Button onPress={showAd} title={"test"} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
