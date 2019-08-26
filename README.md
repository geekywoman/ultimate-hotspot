# Ultimate hotspot

"One Repo to rule them all, One Repo to find them, One Repo to bring them all and in the darkness turn the wifi hotspot on!" 💍

## Description
Demo app to turn your device into a wifi hotspot.

Enabling the wifi hotspot on android can be quite challenging. The android framework which was supporting this feature natively has been slowly but surely deprecating it. If you are looking for possible alternative, you are at the right place. I tried to regroup all wifi hotspot options within one single repo, depending on the android version and if your device is rooted or not.

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
BSD 2-Clause License

Copyright (c) 2019, Geekywoman
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```