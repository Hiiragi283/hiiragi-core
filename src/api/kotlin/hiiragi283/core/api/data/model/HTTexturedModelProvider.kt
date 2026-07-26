package hiiragi283.core.api.data.model

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation

/**
 * @see net.minecraft.data.models.model.TexturedModel.Provider
 */
fun interface HTTexturedModelProvider {
    companion object {
        @JvmField
        val FLAT_ITEM = HTTexturedModelProvider { value: HTIdLike, output: ModelOutput ->
            ModelTemplates.FLAT_ITEM.create(value.itemId, TextureMapping.layer0(value.itemId), output)
        }
    }

    fun create(value: HTIdLike, output: ModelOutput): ResourceLocation
}
