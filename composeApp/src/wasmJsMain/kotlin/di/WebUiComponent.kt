package di

import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.di.SharedUiComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(UiScope::class)
@ContributesSubcomponent(UiScope::class)
interface WebUiComponent : SharedUiComponent {

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun create(): WebUiComponent
    }
}