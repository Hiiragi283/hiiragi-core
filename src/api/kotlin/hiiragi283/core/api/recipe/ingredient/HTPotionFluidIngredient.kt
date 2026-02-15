package hiiragi283.core.api.recipe.ingredient

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.generateHash
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import java.util.stream.Stream

/**
 * [HTPotionFluidManager]に基づいて液体ポーションを扱う[FluidIngredient]の実装クラスです。
 * @param potions 対象となるポーションの一覧
 * @param bottleType 対象となるポーション瓶の種類
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTPotionFluidIngredient(val potions: HolderSet<Potion>, val bottleType: HTBottleType) : FluidIngredient() {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTPotionFluidIngredient> = MapBiCodec.composite(
            VanillaBiCodecs.holderSet(Registries.POTION).fieldOf("potions").forGetter(HTPotionFluidIngredient::potions),
            HTBottleType.CODEC.fieldOf("bottle_type").forGetter(HTPotionFluidIngredient::bottleType),
            ::HTPotionFluidIngredient,
        )

        @JvmField
        val TYPE: FluidIngredientType<HTPotionFluidIngredient> = CODEC.toSerializer(::FluidIngredientType)
    }

    override fun test(fluidStack: FluidStack): Boolean {
        if (HTPotionFluidManager.getBottleType(fluidStack) != bottleType) return false
        return HTPotionFluidManager
            .getContents(fluidStack)
            .potion()
            .map(potions::contains)
            .orElse(false)
    }

    override fun generateStacks(): Stream<FluidStack> = HTPotionFluidManager
        .getSupportedFluids()
        .map { fluid: Holder<Fluid> ->
            val stack = FluidStack(fluid, HTConst.DEFAULT_FLUID_AMOUNT)
            HTPotionFluidManager.setBottleType(fluid.value(), stack, bottleType)
            stack
        }.stream()

    override fun isSimple(): Boolean = false

    override fun getType(): FluidIngredientType<*> = TYPE

    override fun hashCode(): Int = generateHash(potions, bottleType)

    override fun equals(obj: Any?): Boolean = (obj as? HTPotionFluidIngredient)?.let {
        it.potions == this.potions && it.bottleType == this.bottleType
    } ?: false
}
