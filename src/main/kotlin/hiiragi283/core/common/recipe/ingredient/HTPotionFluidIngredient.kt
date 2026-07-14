package hiiragi283.core.common.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.registry.holderSetOf
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.util.HTPhysicalSideHelper
import java.util.Objects
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import java.util.stream.Stream
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidType

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
        val CODEC: MapCodec<HTPotionFluidIngredient> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTCodecs.holderSet(Registries.POTION).fieldOf("potions").forGetter(HTPotionFluidIngredient::potions),
                    HTBottleType.CODEC.fieldOf("bottle_type").forGetter(HTPotionFluidIngredient::bottleType),
                ).apply(instance, ::HTPotionFluidIngredient)
        }

        @JvmField
        val TYPE: FluidIngredientType<HTPotionFluidIngredient> = FluidIngredientType(CODEC)
    }

    constructor(potion: Holder<Potion>) : this(holderSetOf(potion), HTBottleType.DEFAULT)

    override fun test(fluidStack: FluidStack): Boolean {
        val contents: BottledPotionContents = HTPotionHelper.getContents(fluidStack) ?: return false
        if (contents.bottleType != bottleType) return false
        return contents.potion?.let(potions::contains) ?: false
    }

    override fun generateStacks(): Stream<FluidStack> = HTPotionFluidManager
        .fluidHandlers
        .keys
        .flatMap { fluid: Holder<Fluid> ->
            potions
                .filter { it.value().isEnabled(HTPhysicalSideHelper.getFeatureFlags()) }
                .map { potion: Holder<Potion> ->
                    when (potion) {
                        Potions.WATER -> FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME)
                        else -> {
                            val stack = FluidStack(fluid, FluidType.BUCKET_VOLUME)
                            HTPotionHelper.setContents(stack, BottledPotionContents(potion))
                        }
                    }
                }
        }.stream()

    override fun isSimple(): Boolean = false

    override fun getType(): FluidIngredientType<*> = TYPE

    override fun hashCode(): Int = Objects.hash(potions, bottleType)

    override fun equals(obj: Any?): Boolean = (obj as? HTPotionFluidIngredient)?.let {
        it.potions == this.potions && it.bottleType == this.bottleType
    } ?: false

    override fun toString(): String = "HTPotionFluidIngredient(potions=$potions, bottleType=$bottleType)"
}
