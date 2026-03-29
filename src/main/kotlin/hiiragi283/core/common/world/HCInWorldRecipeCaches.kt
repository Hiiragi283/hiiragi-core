package hiiragi283.core.common.world

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCInWorldRecipeCaches : HTValueSerializable {
    val crushing: HTRecipeCache<SingleRecipeInput, HTItemToChancedRecipe.Serializable> = HCRecipeTypes.CRUSHING.createCache()
    val charging: HTRecipeCache<SingleRecipeInput, HTItemToItemRecipe.Serializable> = HCRecipeTypes.CHARGING.createCache()
    val exploding: HTRecipeCache<HCExplodingRecipe.Input, HCExplodingRecipe> = HCRecipeTypes.EXPLODING.createCache()

    override fun serialize(output: HTValueOutput) {
        output.child(HTConst.CRUSHING).let(crushing::serialize)
        output.child(HTConst.CHARGING).let(charging::serialize)
        output.child(HTConst.EXPLODING).let(exploding::serialize)
    }

    override fun deserialize(input: HTValueInput) {
        input.child(HTConst.CRUSHING)?.let(crushing::deserialize)
        input.child(HTConst.CHARGING)?.let(charging::deserialize)
        input.child(HTConst.EXPLODING)?.let(exploding::deserialize)
    }
}
