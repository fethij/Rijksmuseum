![kotlin-version](https://img.shields.io/badge/kotlin-2.0.21-blue?logo=kotlin)
[![Build](https://github.com/fethij/Rijksmuseum/actions/workflows/deploy.yaml/badge.svg)](https://github.com/fethij/Rijksmuseum/actions/workflows/deploy.yaml)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-DB413D.svg?style=flat)
![badge-js](http://img.shields.io/badge/platform-web-FDD835.svg?style=flat)

# Rijksmuseum
![banner](./assets/ios/Rijksmuseum_banner_3.png)

Rijksmuseum is multi-modular Kotlin and Compose Multiplatform app that offers an immersive way to explore the art collection of the renowned Rijksmuseum in Amsterdam.


## Download
<table style="width:100%">
  <tr>
    <td><a href='https://play.google.com/store/apps/details?id=com.tewelde.rijksmuseum&utm_source=github'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=240/></a>
    <td><a href='https://apps.apple.com/ca/app/heirloom-gallery/id6504533291'><img alt='Download in the App Store' src='https://developer.apple.com/assets/elements/badges/download-on-the-app-store.svg' width=180/></a>
  </tr>
</table>


## Design
Design is inspired by [Mari Andrianova](https://dribbble.com/shots/20446337-Gallery-of-art-App).

### iOS
<p style="text-align: center;">
  <img src="assets/ios/ios_0.png" width="250"/>
  <img src="assets/ios/ios_1.png" width="250"/>
  <img src="assets/ios/ios_2.png" width="250"/>
  <img src="assets/ios/ios_3.png" width="250"/>
  <img src="assets/ios/ios_4.png" width="250"/>
  <img src="assets/ios/ios_5.png" width="250"/>
</p>


### Android
<p style="text-align: center;">
  <img src="assets/android/android_0.png" width="250"/>
  <img src="assets/android/android_1.png" width="250"/>
  <img src="assets/android/android_2.png" width="250"/>
  <img src="assets/android/android_3.png" width="250"/>
  <img src="assets/android/android_4.png" width="250"/>
  <img src="assets/android/android_5.png" width="250"/>
</p>



### Desktop
<p style="text-align: center;">
  <img src="assets/desktop/desktop.png" width="613"/>
</p>


### Wasm
<p style="text-align: center;">
  <img src="assets/wasm/wasm.png" width="613"/>
</p>

## Tech Stack 📚
- [Kotlin Multiplatform](https://kotlinlang.org/lp/multiplatform/)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Koin](https://insert-koin.io/)
- [Coil](https://coil-kt.github.io/coil/)
- [Ktor](https://ktor.io/)
- [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation)
- [Jetpack Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
- [Jetpack ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)


## Development :gear:

The app uses Rijksmuseum API, hence you need to get your own API key from [here](https://data.rijksmuseum.nl/object-metadata/api/).
Make sure to add your key to `secrets.properties` file.
In order to sign your builds generate `rijksmuseum.jks` keystore and add keystore password, alias and store password in `secrets.properties`.

secrets.properties would look like this:
```
rijksmuseum.api.key=#Add your Rijksmuseum api key
rijksmuseum.keystore.password=#Add your keystore password
rijksmuseum.key.alias=#Add your key alias
rijksmuseum.key.password=#Add your key password
```

### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  :composeApp --> :core:designsystem
  :composeApp --> :feature:arts
  :core:network --> :core:model
  :core:data --> :core:common
  :core:data --> :core:model
  :core:data --> :core:network
  :core:domain --> :core:common
  :core:domain --> :core:model
  :core:domain --> :core:data
  :core:designsystem --> :core:common
  :feature:arts --> :core:common
  :feature:arts --> :core:model
  :feature:arts --> :core:domain
  :feature:arts --> :core:designsystem
```
