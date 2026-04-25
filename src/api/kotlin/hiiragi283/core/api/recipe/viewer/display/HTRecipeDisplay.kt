package hiiragi283.core.api.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation

interface HTRecipeDisplay : HTIdLike {
    companion object {
        @JvmStatic
        fun <T : HTRecipeDisplay> idCodec(): RecordCodecBuilder<T, ResourceLocation> =
            ResourceLocation.CODEC.fieldOf(HTConst.ID).forGetter(HTRecipeDisplay::getId)
    }

    fun isHandled(): Boolean = true

    open class Simple(private val id: ResourceLocation, val contents: HTRecipeContents) : HTRecipeDisplay {
        companion object {
            @JvmField
            val CODEC: Codec<Simple> = RecordCodecBuilder.create { instance ->
                instance.group(idCodec(), contentsCodec()).apply(instance, ::Simple)
            }

            @JvmStatic
            fun <T : Simple> contentsCodec(): RecordCodecBuilder<T, HTRecipeContents> = HTRecipeContents.CODEC.forGetter(Simple::contents)
        }

        final override fun getId(): ResourceLocation = id
    }
}
