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
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.IntUnaryOperator

/**
 * [HTItemResult]と[HTFluidResult]に関するヘルパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
data object HTResultCreator {
    //    Item    //

    @JvmStatic
    fun create(item: ItemLike, amount: Int = 1): HTItemResult = create(ItemStack(item, amount))

    @JvmStatic
    fun create(stack: ItemStack): HTItemResult = HTItemResult.Simple(stack)

    @JvmStatic
    fun create(tagKey: TagKey<Item>, count: Int = 1): HTItemResult = HTItemResult.Tagged(tagKey, count)

    /**
     * 指定した[部品][part]と[素材][material]から[HTItemResult]の新しいインスタンスを作成します。
     * @since 0.12.0

     @_root_ide_package_.kotlin.jvm.JvmStatic */
    fun material(part: HTPartLike, material: HTMaterialLike, amount: Int = 1): HTItemResult = HTItemResult.MaterialPart(part.asPart(), material.asMaterialKey(), amount)

    //    Fluid    //

    @JvmStatic
    fun create(fluid: Fluid, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(FluidStack(fluid, amount))

    @JvmStatic
    fun create(fluid: HTFluidHolderLike<*>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(fluid.toStack(amount))

    @JvmStatic
    fun create(stack: FluidStack): HTFluidResult = HTFluidResult.create(stack)

    @JvmStatic
    fun water(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.WATER, amount)

    @JvmStatic
    fun lava(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.LAVA, amount)

    @JvmStatic
    fun milk(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.MILK, amount)

    @JvmStatic
    fun molten(material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidResult = material(HTFluidPart.MOLTEN, material, operator)

    @JvmStatic
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
