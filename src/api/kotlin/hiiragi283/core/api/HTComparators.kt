package hiiragi283.core.api

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

object HTComparators {
    @JvmField
    val ID: Comparator<ResourceLocation> =
        compareBy(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath)

    @JvmField
    val KEY: Comparator<ResourceKey<*>> =
        compareBy(ID, ResourceKey<*>::registry).thenComparing(compareBy(ID, ResourceKey<*>::location))

    @JvmField
    val TAG_KEY: Comparator<TagKey<*>> =
        compareBy(KEY, TagKey<*>::registry).thenComparing(compareBy(ID, TagKey<*>::location))
}
