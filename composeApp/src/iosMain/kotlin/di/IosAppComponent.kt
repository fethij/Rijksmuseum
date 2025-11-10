package di

import com.tewelde.rijksmuseum.di.AppComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.reflect.KClass

@SingleIn(AppScope::class)
@MergeComponent(AppScope::class)
abstract class IosAppComponent : AppComponent {

    companion object {
        fun create() = IosAppComponent::class.createComponent()
    }
}

/**
 * The `actual fun` will be generated for each iOS specific target. See [MergeComponent] for more
 * details.
 */
@MergeComponent.CreateComponent
expect fun KClass<IosAppComponent>.createComponent(): IosAppComponent