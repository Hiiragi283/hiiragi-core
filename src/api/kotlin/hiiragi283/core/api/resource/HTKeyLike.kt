package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * [キー][ResourceKey]を提供するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun interface HTKeyLike<R : Any> : HTIdLike {
    fun getKey(): ResourceKey<R>

    fun getRegistryKey(): RegistryKey<R> = getKey().registryKey()

    override fun getId(): ResourceLocation = getKey().location()

    interface Translatable<R : Any> :
        HTKeyLike<R>,
        HTIdLike.Translatable

    fun interface SimpleTranslatable<R : Any> : Translatable<R> {
        override val translationKey: String get() = getKey().toLanguageKey()

        override fun getText(): Text = translatableText(translationKey)
    }
}
