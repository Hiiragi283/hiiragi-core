package hiiragi283.core.client.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.text.toText
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike

object HCJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int = 18 * 4,
        height: Int = 18 * 1,
    ): HTHolderRecipeViewerType<INPUT, RECIPE> = HTHolderRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    @JvmField
    val ANVIL_CRUSHING: HTHolderRecipeViewerType<SingleRecipeInput, HCAnvilCrushingRecipe> =
        create(HCRecipeTypes.ANVIL_CRUSHING, Items.ANVIL, 18 * 5)

    @JvmField
    val CHARGING: HTHolderRecipeViewerType<SingleRecipeInput, HCLightningChargingRecipe> =
        create(HCRecipeTypes.CHARGING, Items.LIGHTNING_ROD)

    @JvmField
    val EXPLODING: HTHolderRecipeViewerType<HCExplodingRecipe.Input, HCExplodingRecipe> =
        create(HCRecipeTypes.EXPLODING, Items.TNT)

    data object MaterialType : HTRecipeViewerType<HTMaterialManager.Entry> {
        override val recipeClass: Class<HTMaterialManager.Entry> = HTMaterialManager.Entry::class.java
        override val icon: Either<ResourceLocation, ItemStack> = Either.Right(ItemStack(Items.IRON_INGOT))
        override val bounds: HTBounds = HTBounds(0, 0, 142, 110)
        override val workStations: List<ItemStack> = emptyList()

        override fun getText(): Component = "Material Parts".toText()

        override fun getId(): ResourceLocation = HiiragiCoreAPI.id("material")
    }
}
