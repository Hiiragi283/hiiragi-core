package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.color.HTColoredCollection
import hiiragi283.core.api.color.VanillaColoredCollections
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.api.util.identity
import hiiragi283.core.common.data.recipe.HCRecipeBuilders
import hiiragi283.core.common.recipe.custom.HTPotionArrowFillingRecipe
import hiiragi283.core.common.recipe.custom.HTPotionTankInteraction
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.support.recipe.cache.HTCompoundRecipeLookup
import hiiragi283.core.support.recipe.cache.HTVanillaRecipeLookup
import hiiragi283.core.support.recipe.cache.fromRecipeType
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

data object HCRecipeLookups {
    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(HiiragiCoreAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTRecipeType<RECIPE>): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    //    Basic    //

    @JvmField
    val BREWING: HTCompoundRecipeLookup<HTItemOrFluidRecipe> = create(HTConst.BREWING)

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
        BREWING.addSubLookup { context: HTRecipeLookup.Context ->
            val brewing: PotionBrewing = context.brewing ?: return@addSubLookup mapOf()
            val recipeMap: MutableMap<ResourceLocation, HCBrewingRecipe> = mutableMapOf()
            for ((potionTo: Holder<Potion>, mixes: List<PotionBrewing.Mix<Potion>>) in brewing.potionMixes.groupBy(PotionBrewing.Mix<Potion>::to)) {
                mixes.forEachIndexed { index: Int, mix: PotionBrewing.Mix<Potion> ->
                    HCRecipeBuilders.brewing {
                        itemIngredient { +mix.ingredient }
                        fluidIngredient { +HTPotionFluidIngredient(mix.from()) }
                        fluidResult { +BottledPotionContents(mix.to()).let(HCPotionFluidHelper::createFluid) }
                    }.save { _, recipe ->
                        if (recipe.isIncomplete) return@save
                        recipeMap[potionTo.toLike().getId().withSuffix("_$index")] = recipe
                    }
                }
            }
            recipeMap
        }
        BREWING.fromRecipeType(HCRecipeTypes.BREWING, identity())

        CRUSHING.fromRecipeType(HCRecipeTypes.CRUSHING, identity())

        EMPTYING.fromRecipeType(HCRecipeTypes.EMPTYING, identity())
        EMPTYING.addRecipes(vanillaId(HTConst.EMPTYING, "potion") to HTPotionTankInteraction.Emptying)

        FILLING.fromRecipeType(HCRecipeTypes.FILLING, identity())
        FILLING.addRecipes(vanillaId(HTConst.FILLING, "potion") to HTPotionTankInteraction.Filling)
        FILLING.addRecipes(vanillaId(HTConst.FILLING, "potion_arrow") to HTPotionArrowFillingRecipe)

        coloring(ItemTags.BANNERS, VanillaColoredCollections.BANNER)
        coloring(ItemTags.BEDS, VanillaColoredCollections.BED)
        coloring(ItemTags.WOOL, VanillaColoredCollections.WOOL)
        coloring(ItemTags.WOOL_CARPETS, VanillaColoredCollections.CARPET)
        coloring(Tags.Items.SHULKER_BOXES, VanillaColoredCollections.SHULKER_BOX)
    }

    @JvmStatic
    fun coloring(inputTag: TagKey<Item>, contents: HTColoredCollection<SupplierWithId<ItemLike>>) {
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
