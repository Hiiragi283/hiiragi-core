package hiiragi283.core.common.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.common.item.HTBlueprintItem
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCItems
import java.util.stream.Stream
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType

class HTBluePrintIngredient(private val number: Int) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBluePrintIngredient> =
            HTBlueprintItem.CODEC.fieldOf("number").xmap(::HTBluePrintIngredient, HTBluePrintIngredient::number)

        @JvmField
        val TYPE: IngredientType<HTBluePrintIngredient> = IngredientType(CODEC)
    }

    override fun test(stack: ItemStack): Boolean = stack.get(HCDataComponents.BLUEPRINT_NUMBER) == number

    override fun getItems(): Stream<ItemStack> = Stream.of(createItemStack(HCItems.BLUEPRINT, HCDataComponents.BLUEPRINT_NUMBER, number))

    override fun isSimple(): Boolean = false

    override fun getType(): IngredientType<*> = TYPE
}
