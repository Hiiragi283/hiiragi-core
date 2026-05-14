package hiiragi283.lib.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.HTIdLike
import net.minecraft.resources.Identifier

interface HTRecipeDisplay : HTIdLike {
    companion object {
        @JvmStatic
        fun <T : HTRecipeDisplay> idCodec(): RecordCodecBuilder<T, Identifier> = Identifier.CODEC.fieldOf(HTConstants.ID).forGetter(HTRecipeDisplay::getId)
    }

    fun isHandled(): Boolean = true

    open class Simple(private val id: Identifier, val contents: HTRecipeContents) : HTRecipeDisplay {
        companion object {
            @JvmField
            val CODEC: Codec<Simple> = RecordCodecBuilder.create { it.group(idCodec(), contentsCodec()).apply(it, ::Simple) }

            @JvmStatic
            fun <T : Simple> contentsCodec(): RecordCodecBuilder<T, HTRecipeContents> = HTRecipeContents.CODEC.forGetter(Simple::contents)
        }

        final override fun getId(): Identifier = id
    }
}

//    Extensions    //

@Suppress("FunctionName")
inline fun HTRecipeDisplay(id: Identifier, builderAction: HTRecipeContents.Builder.() -> Unit): HTRecipeDisplay.Simple = HTRecipeDisplay.Simple(id, HTRecipeContents.create(builderAction))
