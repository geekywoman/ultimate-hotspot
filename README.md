# Ultimate hotspot

"One Repo to rule them all, One Repo to find them, One Repo to bring them all and in the darkness turn the wifi hotspot on!" 💍

## Description
Demo app to turn your device into a wifi hotspot.

Enabling the wifi hotspot on android can be quite challenging. The android framework which was natively supporting this feature has been slowly but surely deprecating it. If you are looking for possible alternatives, you are at the right place. I tried to regroup all wifi hotspot options within one single repo, whether your device is rooted or not.

Hope if you come across this repo, it might help you in your wifi hotspot quest :)

## Wifi hotspot alternatives, with related pros and cons.
If you are running Android Marshmallow or below, device rooted or not, take a look to [Marshmallow Manager](/app/src/main/java/com/origo/ultimatehotspot/manager/hotspot/MarshmallowWifiManager.kt)
- 👍 uses the android framework hotspot, making this solution more reliable
- 👍 your device do not need to be rooted
- 👎 works only under marshmallow

If your device is rooted, take a look to  [Direct Share Manager](/app/src/main/java/com/origo/ultimatehotspot/manager/hotspot/DirectShareManager.kt)
- 👍 works on any android version
- 👎 the devices has to be rooted, otherwise the user needs to setup the proxy manually on any device he wants to connect to the hotspot created by Direct Share
- 👎 you do not have the possibility to setup the password or the ssid of the hotspot

If you are running Android Oreo or above, device rooted or not, take a look to [Oreo Wifi Manager](/app/src/main/java/com/origo/ultimatehotspot/manager/hotspot/OreoWifiManager.kt)
- 👍 your device do not need to be rooted
- 👎 works only above oreo

## Tests
This demo has been tested on the following devices:
- Lenovo Tab3, Android 6.0
- Lenovo Tab4, Android 7.1.1
- Samsung Active 2, Android 8.+
- Samsung Tab A (2019), Android 9

## Acknowledgement
A special thanks to [Direct-share](https://github.com/shinilms/direct-net-share) and [Oreo wifi hotspot](https://github.com/aegis1980/WifiHotSpot) repositories

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.