![kotlin-version](https://img.shields.io/badge/kotlin-2.0.20-blue?logo=kotlin)
[![Build](https://github.com/fethij/Rijksmuseum/actions/workflows/deploy.yaml/badge.svg)](https://github.com/fethij/Rijksmuseum/actions/workflows/deploy.yaml)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-DB413D.svg?style=flat)
![badge-js](http://img.shields.io/badge/platform-web-FDD835.svg?style=flat)

# Rijksmuseum

<img alt="s1" src="https://github.com/fethij/Rijksmuseum/assets/32542424/f4906a9b-678d-4fa5-a51c-ee94c9ef0147">
Rijksmuseum is multi-modular Kotlin and Compose Multiplatform app that offers an immersive way to explore the art collection of the renowned Rijksmuseum in Amsterdam.


## Download

<a href='https://play.google.com/store/apps/details?id=com.tewelde.rijksmuseum&utm_source=github'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' width="200px"/></a>


## Design
Design is inspired by [Mari Andrianova](https://dribbble.com/shots/20446337-Gallery-of-art-App).

### iOS
<img width="240" alt="s1" src="https://github.com/fethij/Rijksmuseum/assets/32542424/0b73f4a6-d446-4a2f-93dc-7b3aac9bc056">
<img width="240" alt="s1" src="https://github.com/fethij/Rijksmuseum/assets/32542424/0351f79c-d1cf-466b-a5a3-7d9dd0c07120">
<img width="240" alt="s1" src="https://github.com/fethij/Rijksmuseum/assets/32542424/9f3de760-4856-4681-9ea2-b56d6553521c">

<img width="240" alt="s1" src="https://github.com/fethij/Rijksmuseum/assets/32542424/eb25dbef-96e4-4602-b545-8d240f844485">
<img width="240" alt="s1" src="https://github.com/fethij/Rijksmuseum/assets/32542424/69ed3a3d-b403-4200-af19-1b487f1b3654">
<img width="240" alt="s1" src="https://github.com/fethij/Rijksmuseum/assets/32542424/813cf837-b4d6-4564-ad12-50461c3f2b35">


### Android
<img width="240" alt="s2" src="https://github.com/fethij/ArtGalleryViewer/assets/32542424/a46b7b5b-d2d3-4d27-a897-5c81ae6439c7">
<img width="240" alt="s1" src="https://github.com/fethij/ArtGalleryViewer/assets/32542424/2056de1e-6841-4455-bc95-6ffbdaec1542">
<img width="240" alt="s2" src="https://github.com/fethij/ArtGalleryViewer/assets/32542424/241f9f7e-f926-4885-a448-b2a6769c7518">

<img width="240" alt="s1" src="https://github.com/fethij/ArtGalleryViewer/assets/32542424/4cb65274-aa61-4c78-9439-874d655a2295">
<img width="240" alt="s2" src="https://github.com/fethij/ArtGalleryViewer/assets/32542424/c8a96798-e180-4902-aba7-58602167bde8">
<img width="240" alt="s2" src="https://github.com/fethij/ArtGalleryViewer/assets/32542424/0db53073-29bb-4cf8-8f4c-3dcf3fe64fb3">


### Desktop
<img width="613" alt="s2" src="https://github.com/fethij/Rijksmuseum/assets/32542424/9b25b3f4-e82d-4468-bc65-62b84d19c691">


### Wasm
<img width="613" alt="s2" src="https://github.com/fethij/Rijksmuseum/assets/32542424/f0585d06-97fb-49c5-a014-ca03907c4a89">


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
