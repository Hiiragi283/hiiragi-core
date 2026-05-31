package hiiragi283.core.common.recipe.ingredient

import com.mojang.serialization.MapCodec
import java.util.stream.Stream
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType

data object HTDamageableIngredient : ICustomIngredient {
    @JvmField
    val CODEC: MapCodec<HTDamageableIngredient> = MapCodec.unit { HTDamageableIngredient }

    @JvmField
    val TYPE: IngredientType<HTDamageableIngredient> = IngredientType(CODEC)

    override fun test(stack: ItemStack): Boolean = stack.isDamageableItem

    override fun items(): Stream<Holder<Item>> = BuiltInRegistries.ITEM.listElements().filter { ItemStack(it).isDamageableItem }.map { it as Holder<Item> }

    override fun isSimple(): Boolean = false

    override fun getType(): IngredientType<*> = TYPE
}
