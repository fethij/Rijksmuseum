package di

import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.di.SharedUiComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(UiScope::class)
@ContributesSubcomponent(UiScope::class)
interface IosUiComponent : SharedUiComponent {

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun create(): IosUiComponent
    }
}
