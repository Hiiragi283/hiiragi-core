package hiiragi283.core.api

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[Comparator]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
data object HTComparators {
    /**
     * [ID][ResourceLocation]の[Comparator]
     */
    @JvmField
    val ID: Comparator<ResourceLocation> =
        compareBy(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath)

    /**
     * [ResourceKey]の[Comparator]
     */
    @JvmField
    val KEY: Comparator<ResourceKey<*>> =
        compareBy(ID, ResourceKey<*>::registry).thenComparing(compareBy(ID, ResourceKey<*>::location))

    /**
     * [TagKey]の[Comparator]
     */
    @JvmField
    val TAG_KEY: Comparator<TagKey<*>> =
        compareBy(KEY, TagKey<*>::registry).thenComparing(compareBy(ID, TagKey<*>::location))
}
