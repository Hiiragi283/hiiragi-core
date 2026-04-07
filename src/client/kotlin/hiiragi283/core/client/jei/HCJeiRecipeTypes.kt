package hiiragi283.core.client.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.recipe.HTDoubleItemToMultiOutputRecipe
import hiiragi283.core.api.recipe.HTSingleItemRecipe
import hiiragi283.core.api.recipe.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText
import hiiragi283.core.api.util.Either
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike

object HCJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(
        recipeType: HTRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTLookupRecipeViewerType<INPUT, RECIPE> = HTLookupRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    @JvmField
    val BREWING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, HCBrewingRecipe> =
        create(HTVanillaRecipeTypes.BREWING, Items.BREWING_STAND, 18 * 6)

    @JvmField
    val CHARGING: HTLookupRecipeViewerType<SingleRecipeInput, HTSingleItemRecipe> =
        create(HCRecipeLookups.CHARGING, Items.LIGHTNING_ROD, 18 * 4)

    @JvmField
    val CRUSHING: HTLookupRecipeViewerType<SingleRecipeInput, HTSingleMultiOutputRecipe> =
        create(HCRecipeLookups.CRUSHING, Items.ANVIL, 18 * 5, 18 * 2)

    @JvmField
    val EXPLODING: HTLookupRecipeViewerType<HCExplodingRecipe.Input, HCExplodingRecipe> =
        create(HCRecipeLookups.EXPLODING, Items.TNT, 18 * 6)

    @JvmField
    val FORGING: HTLookupRecipeViewerType<HTDoubleRecipeInput, HTDoubleItemToMultiOutputRecipe> =
        create(HCRecipeLookups.FORGING, Items.ANVIL, 18 * 6, 18 * 3)

    @JvmField
    val MELTING: HTLookupRecipeViewerType<HCMeltingRecipe.Input, HCMeltingRecipe> =
        create(HCRecipeLookups.MELTING, Items.LAVA_BUCKET, 18 * 4)

    @JvmField
    val TANK_INTERACTION: HTLookupRecipeViewerType<RecipeInput, HTTankInteraction> =
        create(HCRecipeLookups.TANK_INTERACTION, Items.CAULDRON, 18 * 5, 18 * 3)

    data object MaterialType : HTRecipeViewerType<HTMaterialManager.Entry> {
        override val recipeClass: Class<HTMaterialManager.Entry> = HTMaterialManager.Entry::class.java
        override val icon: Either<ResourceLocation, ItemStack> = Either.Right(ItemStack(Items.IRON_INGOT))
        override val bounds: HTBounds = HTBounds(0, 0, 142, 110)
        override val workStations: List<ItemStack> = emptyList()

        override fun getText(): Text = "Material Parts".toText()

        override fun getId(): ResourceLocation = HiiragiCoreAPI.id("material")
    }
}
