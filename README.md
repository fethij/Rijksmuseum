# Rijksmuseum

Rijksmuseum is a multiplatform app using the Rijksmuseum [rich and freely accessible content](https://www.rijksmuseum.nl/en/data/policy) api. 
You can filter arts based on location.

Rijksmuseum is a multiplatform app built using Kotlin and Compose Multiplatform. It features an nice
user interface and experience to browse through arts.


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

## Development 🛠️

You can just clone the repo and build it locally without requiring any changes. 

Get your own Api Key from [Rijksmuseum.nl](https://data.rijksmuseum.nl/object-metadata/api/).

Project requires JDK 17+, and based on the AGP version defined in [`libs.versions.toml`](/gradle/libs.versions.toml) file, 
you can use appropriate Android Studio/Fleet to import the project.

### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  :composeApp --> :core:domain
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
