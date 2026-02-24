package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import org.apache.commons.lang3.math.Fraction

class HCExplodingRecipe(ingredient: HTItemIngredient, result: HTItemResult, val minPower: Fraction) :
    HCSingleItemRecipe<HCExplodingRecipe.Input>(ingredient, result) {
    companion object {
        @JvmStatic
        fun createIcon(power: Float): ItemStack {
            val item: Item = when {
                power > 4 -> Items.END_CRYSTAL
                power > 3 && power <= 4 -> Items.TNT
                power > 1 -> Items.CREEPER_SPAWN_EGG
                else -> Items.FIRE_CHARGE
            }
            return createItemStack(
                item,
                DataComponents.ITEM_NAME,
                HCTranslation.MIN_POWER.translateColored(HTDefaultColor.YELLOW, HTDefaultColor.WHITE, power),
            )
        }
    }

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EXPLODING

    override fun getType(): RecipeType<*> = HCRecipeTypes.EXPLODING.get()

    override fun test(input: Input): Boolean = ingredient.test(input.item) && input.power >= minPower

    @JvmRecord
    data class Input(val item: ItemStack, val power: Fraction) : RecipeInput {
        override fun getItem(index: Int): ItemStack = item

        override fun size(): Int = 1
    }
}
