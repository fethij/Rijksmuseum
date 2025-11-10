package di

import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.di.SharedUiComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(UiScope::class)
interface DesktopUiComponent : SharedUiComponent {

    @GraphExtension.Factory
    @ContributesTo(AppScope::class)
    interface Factory {
        fun create(): DesktopUiComponent
    }
}
