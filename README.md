# Ultimate hotspot

"One Repo to rule them all, One Repo to find them, One Repo to bring them all and in the darkness turn the wifi hotspot on!" 💍

## Description
Demo app to turn your device into a wifi hotspot.

Enabling the wifi hotspot on android can be quite challenging. The android framework which was supporting this feature natively has been slowly but surely deprecating it. If you are looking for possible alternatives, you are at the right place. I tried to regroup all wifi hotspot options within one single repo, whether your device is rooted or not.

Hope if you come across this repo, it might help you in your wifi hotspot quest :)

## Wifi hotspot alternatives, with related pros and cons.
If you are running Android Marshmallow or below, device rooted or not, take a look to [Marshmallow Manager](com.origo.ultimatehotspot.manager.hotspot.MarshmallowWifiManager)
- 👍 uses the android framework hotspot, making this solution more reliable
- 👍 your device do not need to be rooted
- 👎 works only under marshmallow

If your device is rooted, take a look to  [Direct Share Manager](com.origo.ultimatehotspot.manager.hotspot.DirectShareManager)
- 👍 works on any android version
- 👎 the devices has to be rooted, otherwise the user needs to setup the proxy manually on any device he wants to connect to the hotspot created by Direct Share
- 👎 you do not have the possibility to setup the password or the ssid of the hotspot

If you are running Android Oreo or above, device rooted or not, take a look to [Oreo Wifi Manager](com.origo.ultimatehotspot.manager.hotspot.OreoWifiManager)
- 👍 your device do not need to be rooted
- 👎 works only above oreo

## Tests
This demo has been tested on the following devices:
- Lenovo Tab3, Android 6.0
- Lenovo Tab4, Android 7.1.1
- Samsung Active 2, Android 8.+
- Samsung Tab A (2019), Android 9

## Acknowledgement
A special thanks to [Direct-share](https://github.com/shinilms/direct-net-share) and [Oreo wifi hotspot](https://github.com/aegis1980/WifiHotSpot)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
```
MIT License

Copyright (c) [2019] [geekywoman]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```