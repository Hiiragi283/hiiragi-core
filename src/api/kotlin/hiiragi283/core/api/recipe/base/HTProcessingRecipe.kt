package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.ParameterCodec
import io.netty.buffer.ByteBuf
import net.minecraft.world.item.crafting.RecipeInput

interface HTProcessingRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    companion object {
        @JvmStatic
        fun <RECIPE : HTProcessingRecipe<*>> timeCodec(): ParameterCodec<ByteBuf, RECIPE, Int> =
            BiCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.TIME).forGetter { it.time }
    }

    val time: Int

    interface Serializable<INPUT : RecipeInput> :
        HTProcessingRecipe<INPUT>,
        HTSerializableRecipe<INPUT>
}
