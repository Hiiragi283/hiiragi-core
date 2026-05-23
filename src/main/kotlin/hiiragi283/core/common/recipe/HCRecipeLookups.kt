package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTColoredContents
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.VanillaColoredContents
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.HTDeferredRecipeType
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.recipe.custom.HTPotionArrowFillingRecipe
import hiiragi283.core.common.recipe.custom.HTPotionTankInteraction
import hiiragi283.core.impl.recipe.cache.HTCompoundRecipeLookup
import hiiragi283.core.impl.recipe.cache.HTVanillaRecipeLookup
import hiiragi283.core.impl.recipe.cache.fromRecipeType
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

data object HCRecipeLookups {
    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(HiiragiCoreAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTDeferredRecipeType<RECIPE>): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    //    Basic    //

    @JvmField
    val CHARGING: HTRecipeLookup<HCChargingRecipe> = create(HCRecipeTypes.CHARGING)

    @JvmField
    val CRUSHING: HTCompoundRecipeLookup<HTItemToMultiItemRecipe> = create(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTRecipeLookup<HCExplodingRecipe> = create(HCRecipeTypes.EXPLODING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTCompoundRecipeLookup<HTTankEmptyingRecipe> = create(HTConst.EMPTYING)

    @JvmField
    val FILLING: HTCompoundRecipeLookup<HTTankFillingRecipe> = create(HTConst.FILLING)

    //    Registration    //

    @JvmStatic
    fun init() {
        CRUSHING.fromRecipeType(HCRecipeTypes.CRUSHING.get(), identity())

        EMPTYING.fromRecipeType(HCRecipeTypes.EMPTYING.get(), identity())
        EMPTYING.addRecipes(HTConst.MINECRAFT.toId(HTConst.EMPTYING, "potion") to HTPotionTankInteraction.Emptying)

        FILLING.fromRecipeType(HCRecipeTypes.FILLING.get(), identity())
        FILLING.addRecipes(HTConst.MINECRAFT.toId(HTConst.FILLING, "potion") to HTPotionTankInteraction.Filling)
        FILLING.addRecipes(HTConst.MINECRAFT.toId(HTConst.FILLING, "potion_arrow") to HTPotionArrowFillingRecipe)

        coloring(ItemTags.BANNERS, VanillaColoredContents.BANNER)
        coloring(ItemTags.BEDS, VanillaColoredContents.BED)
        coloring(ItemTags.WOOL, VanillaColoredContents.WOOL)
        coloring(ItemTags.WOOL_CARPETS, VanillaColoredContents.CARPET)
        coloring(Tags.Items.SHULKER_BOXES, VanillaColoredContents.SHULKER_BOX)
    }

    @JvmStatic
    fun coloring(inputTag: TagKey<Item>, contents: HTColoredContents<SupplierWithId<ItemLike>>) {
        /*COLORING.addProvider {
            sequenceOf(
                HTRecipeHolder(
                    inputTag.location().withPrefix("${HTConst.FILLING}/coloring/"),
                    HCColoringRecipe(inputTag, contents),
                ),
            )
        }*/
    }
}
