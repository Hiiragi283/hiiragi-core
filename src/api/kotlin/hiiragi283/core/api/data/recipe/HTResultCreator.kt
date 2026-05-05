package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.VanillaFluidContents
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction
import java.util.function.IntUnaryOperator

/**
 * [HTItemResult]と[HTFluidResult]に関するヘルパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTResultCreator(provider: HolderLookup.Provider) {
    private val itemGetter: HolderGetter<Item> = provider.lookupOrThrow(Registries.ITEM)

    //    Item    //

    fun create(item: ItemLike, amount: Int = 1, chance: Fraction = Fraction.ONE): HTItemResult = create(ItemStack(item, amount), chance)

    fun create(stack: ItemStack, chance: Fraction = Fraction.ONE): HTItemResult = HTItemResult(stack, chance)

    fun create(tagKey: TagKey<Item>, count: Int = 1, chance: Fraction = Fraction.ONE): HTItemResult =
        HTItemResult(HTItemResult.TagEntry(itemGetter.getOrThrow(tagKey), count), chance)

    /**
     * 指定した[部品][part]と[素材][material]から[HTItemResult]の新しいインスタンスを作成します。
     * @since 0.12.0
     */
    fun material(
        part: HTPartLike,
        material: HTMaterialLike,
        amount: Int = 1,
        chance: Fraction = Fraction.ONE,
    ): HTItemResult = HTItemResult(HTItemResult.MaterialPartEntry(part, material, amount), chance)

    //    Fluid    //

    fun create(fluid: Fluid, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(FluidStack(fluid, amount))

    fun create(fluid: HTFluidHolderLike<*>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(fluid.toStack(amount))

    fun create(stack: FluidStack): HTFluidResult = HTFluidResult.create(stack)

    fun water(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.WATER, amount)

    fun lava(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.LAVA, amount)

    fun milk(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.MILK, amount)

    fun molten(material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidResult =
        material(HTFluidPart.MOLTEN, material, operator)

    fun material(part: HTFluidPart, material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidResult {
        val fluid: Fluid = HiiragiCoreAccess.INSTANCE.registeredFluids
            .getOrThrow(part, material)
            .get()
        return HTMaterialManager
            .getInstance()
            .getOrEmpty(material)
            .getDefaultFluidAmount()
            .let(operator::applyAsInt)
            .let { create(fluid, it) }
    }
}
