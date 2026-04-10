package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.data.tank.HTColoringTankInteraction
import hiiragi283.core.common.data.tank.HTPotionArrowTankInteraction
import hiiragi283.core.common.data.tank.HTPotionTankInteraction
import hiiragi283.core.common.event.HCRecipeEventHandler
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.impl.recipe.HTRecipeTypeImpl
import hiiragi283.core.impl.recipe.HTRecipeTypeManager
import hiiragi283.core.impl.recipe.addProvider
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.Tags

data object HCRecipeLookups {
    @JvmField
    val CHARGING: HTRecipeTypeImpl<SingleRecipeInput, HTSingleItemRecipe> = create(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTRecipeTypeImpl<SingleRecipeInput, HTSingleMultiOutputRecipe> = create(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTRecipeTypeImpl<HCExplodingRecipe.Input, HCExplodingRecipe> = create(HTConst.EXPLODING)

    @JvmField
    val FORGING: HTRecipeTypeImpl<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> = create(HTConst.FORGING)

    @JvmField
    val MELTING: HTRecipeTypeImpl<HCMeltingRecipe.Input, HCMeltingRecipe> = create(HTConst.MELTING)

    @JvmField
    val TANK_INTERACTION: HTRecipeTypeImpl<RecipeInput, HTTankInteraction> = create(HTConst.TANK_INTERACTION) // TODO

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(path: String): HTRecipeTypeImpl<INPUT, RECIPE> =
        HTRecipeTypeManager.create(HiiragiCoreAPI.id(path))

    @JvmStatic
    fun init() {
        CHARGING.addProvider(HCRecipeTypes.CHARGING.get(), identity())

        CRUSHING.addProvider(HCRecipeTypes.CRUSHING.get(), identity())

        EXPLODING.addProvider(HCRecipeTypes.EXPLODING.get(), identity())

        FORGING.addProvider(HCRecipeTypes.FORGING.get(), identity())

        MELTING.addProvider(HCRecipeTypes.MELTING.get(), identity())

        TANK_INTERACTION.addProvider { HCRecipeEventHandler.tankInteractionMap.asSequence().map(::HTRecipeHolder) }
        TANK_INTERACTION.addProvider(HTConst.MINECRAFT.toId("potion") to HTPotionTankInteraction)
        TANK_INTERACTION.addProvider(HTConst.MINECRAFT.toId("tipped_arrow") to HTPotionArrowTankInteraction)

        dyesItems(ItemTags.BANNERS, ColoredMaterials.BANNER)
        dyesItems(ItemTags.BEDS, ColoredMaterials.BED)
        dyesItems(ItemTags.WOOL, ColoredMaterials.WOOL)
        dyesItems(ItemTags.WOOL_CARPETS, ColoredMaterials.CARPET)
        dyesItems(Tags.Items.SHULKER_BOXES, ColoredMaterials.SHULKER_BOX)
    }

    @JvmStatic
    private fun dyesItems(inputTag: TagKey<Item>, map: Map<HTDefaultColor, HTSimpleItemHolderLike>) {
        TANK_INTERACTION.addProvider {
            map
                .asSequence()
                .map { (color: HTDefaultColor, colored: HTSimpleItemHolderLike) ->
                    HTRecipeHolder(
                        colored.getId().withPrefix("${HTConst.TANK_INTERACTION}/coloring/"),
                        HTColoringTankInteraction(inputTag, HCFluids.getDye(color), colored.toStack()),
                    )
                }
        }
    }
}
