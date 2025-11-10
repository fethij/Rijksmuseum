package com.tewelde.rijksmuseum.core.common.di

object ComponentHolder {

    val components = mutableSetOf<Any>()

    /**
     * Fetch a component of type [T] that has been added to the holder, automatically casting
     * it in the return.
     */
    inline fun <reified T> component(): T = components
        .filterIsInstance<T>()
        .firstOrNull()
        ?: throw NoSuchElementException("No component found for '${T::class.qualifiedName}'")

    /**
     * Update a component of the given type, [T], in the component holder
     */
    fun <T : Any> updateComponent(component: T) {
        components.removeAll { it::class.isInstance(component) }
        components += component
    }
}
