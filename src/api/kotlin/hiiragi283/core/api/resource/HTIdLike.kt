package hiiragi283.core.api.resource

import net.minecraft.resources.ResourceLocation

fun interface HTIdLike {
    fun getId(): ResourceLocation

    fun getIdWithPrefix(prefix: String): ResourceLocation = getId().withPrefix(prefix)

    fun getIdWithSuffix(suffix: String): ResourceLocation = getId().withSuffix(suffix)

    fun getPath(): String = getId().path
}
