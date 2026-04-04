package hiiragi283.core.api.material

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentInitializers
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.util.Util
import net.neoforged.neoforge.common.CommonHooks

/**
 * @see net.minecraft.world.item.Item
 */
open class HTMaterial(properties: Properties) :
    DataComponentHolder,
    HTMaterialLike,
    HTHasTranslationKey,
    HTHasText {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, Holder<HTMaterial>> = VanillaBiCodecs.holder(HCRegistries.Keys.MATERIAL)
    }

    val buildInRegistryHolder: Holder.Reference<HTMaterial> = HCRegistries.MATERIAL.createIntrusiveHolder(this)
    private val key: ResourceKey<HTMaterial> = properties.materialIdOrThrow()
    val tagName: Set<String> = properties.tagNames + key.identifier().path
    override val translationKey: String = Util.makeDescriptionId(HTConst.MATERIAL, key.identifier())

    init {
        val initializer: DataComponentInitializers.Initializer<HTMaterial> =
            properties.finalizeInitializer(translatableText(translationKey))
        BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.add(key, initializer)
    }

    override fun getComponents(): DataComponentMap = buildInRegistryHolder.components()

    override fun asMaterial(): HTMaterial = this

    override fun getText(): Text = getOrDefault(HTMaterialComponentAccess.INSTANCE.materialName(), Text.empty())

    //    Properties    //

    /**
     * @see net.minecraft.world.item.Item.Properties
     */
    class Properties {
        private var id: ResourceKey<HTMaterial>? = null
        internal val tagNames: MutableSet<String> = mutableSetOf()
        private var componentInitializer = DataComponentInitializers.Initializer<HTMaterial> { _, _, _ -> }

        fun setId(key: ResourceKey<HTMaterial>): Properties = apply { this.id = key }

        fun materialIdOrThrow(): ResourceKey<HTMaterial> = checkNotNull(id) { "Material id not set" }

        // components
        fun tagNames(vararg names: String): Properties = apply { tagNames += names }

        fun <T : Any> component(type: DataComponentType<T>, value: T): Properties = apply {
            this.componentInitializer = this.componentInitializer.add(type, value)
        }

        fun <T : Any> delayedComponent(
            type: DataComponentType<T>,
            initializer: DataComponentInitializers.SingleComponentInitializer<T>,
        ): Properties = apply {
            this.componentInitializer = this.componentInitializer.andThen(initializer.asInitializer(type))
        }

        @Suppress("UnstableApiUsage")
        fun <T : Any> delayedHolderComponent(type: DataComponentType<Holder<T>>, valueKey: ResourceKey<T>): Properties = apply {
            this.componentInitializer =
                this.componentInitializer.andThen { builder: DataComponentMap.Builder, provider: HolderLookup.Provider, _ ->
                    val value: Holder.Reference<T> = provider.getOrThrow(valueKey)
                    CommonHooks.validateComponent(value)
                    builder.set(type, value)
                }
        }

        internal fun finalizeInitializer(name: Text): DataComponentInitializers.Initializer<HTMaterial> =
            componentInitializer.andThen { builder: DataComponentMap.Builder, _, _ ->
                builder.set(HTMaterialComponentAccess.INSTANCE.materialName(), name)
            }
    }
}
