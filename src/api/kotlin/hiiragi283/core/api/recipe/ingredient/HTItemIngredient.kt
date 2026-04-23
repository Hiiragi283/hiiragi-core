package hiiragi283.core.api.recipe.ingredient

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

/**
 * [ItemStack]向けに[HTIngredient]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTItemIngredient(val unsized: Ingredient, val count: Int) : HTIngredient.Stacked<ItemStack, HTItemResourceType> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemIngredient> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.ITEMS).forGetter(HTItemIngredient::unsized),
                    HTCodecs.NON_NEGATIVE_INT
                        .fieldOf(HTConst.AMOUNT)
                        .orElse(1)
                        .forGetter(HTItemIngredient::count),
                ).apply(instance, ::HTItemIngredient)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemIngredient> = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HTItemIngredient::unsized,
            ByteBufCodecs.VAR_INT,
            HTItemIngredient::count,
            ::HTItemIngredient,
        )
    }

    init {
        require(unsized.isEmpty) { "Ingredient must not be empty" }
        require(count > 0) { "Ingredient count must be positive" }
    }

    override fun test(stack: ItemStack): Boolean = testOnlyType(stack) && stack.count >= count

    override fun testOnlyType(stack: ItemStack): Boolean = unsized.test(stack)

    override fun getRequiredAmount(stack: ItemStack): Int = when {
        testOnlyType(stack) -> count
        else -> 0
    }

    override fun test(resource: HTItemResourceType, amount: Int): Boolean = resource.toStack(amount).let(::test)

    override fun getRequiredAmount(resource: HTItemResourceType, amount: Int): Int = resource.toStack(amount).let(::getRequiredAmount)
}
