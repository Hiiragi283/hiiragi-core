package hiiragi283.core.common.world

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCInWorldRecipeCaches : HTValueSerializable {
    val crushing: HTLookupRecipeCache<SingleRecipeInput, HTSingleMultiOutputRecipe> =
        HTLookupRecipeCache.forRecipe(HCRecipeLookups.CRUSHING)
    val charging: HTLookupRecipeCache<HCChargingRecipe.Input, HCChargingRecipe> =
        HTLookupRecipeCache.forRecipe(HCRecipeLookups.CHARGING)
    val exploding: HTRecipeCache<HCExplodingRecipe.Input, HCExplodingRecipe> =
        HTLookupRecipeCache.forRecipe(HCRecipeLookups.EXPLODING)

    override fun serialize(output: HTValueOutput) {
        output.write(HTConst.CRUSHING, crushing)
        output.write(HTConst.CHARGING, charging)
        output.write(HTConst.EXPLODING, exploding)
    }

    override fun deserialize(input: HTValueInput) {
        input.read(HTConst.CRUSHING, crushing)
        input.read(HTConst.CHARGING, charging)
        input.read(HTConst.EXPLODING, exploding)
    }
}
