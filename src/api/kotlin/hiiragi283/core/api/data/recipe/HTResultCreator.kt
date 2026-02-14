package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
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
    fun create(item: ItemLike, amount: Int = 1): HTItemResult = HTItemResult.create {
        this.item = item.toResource()
        this.amount = amount
    }

    @JvmStatic
    fun create(stack: ItemStack): HTItemResult = HTItemResult.create {
        item = stack.toResource()
        amount = stack.count
    }

    @JvmStatic
    fun material(prefix: HTTagPrefix, material: HTMaterialLike, amount: Int = 1): HTItemResult = HTItemResult.create {
        this.item = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(prefix, material).toResource()
        this.tagKey = prefix.itemTagKey(material)
        this.amount = amount
    }

    //    Fluid    //

    @JvmStatic
    fun create(fluid: Fluid, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(FluidStack(fluid, amount))

    @JvmStatic
    fun create(fluid: HTFluidHolderLike<*>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult =
        HTFluidResult(checkNotNull(fluid.toResource()), amount)

    @JvmStatic
    fun create(stack: FluidStack): HTFluidResult = HTFluidResult(checkNotNull(stack.toResource()), stack.amount)

    @JvmStatic
    fun water(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.WATER, amount)

    @JvmStatic
    fun lava(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.LAVA, amount)

    @JvmStatic
    fun milk(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.MILK, amount)

    @JvmStatic
    fun molten(material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidResult =
        material(CommonFluidTagPrefixes.MOLTEN, material, operator)

    @JvmStatic
    fun material(
        prefix: HTFluidTagPrefix,
        material: HTMaterialLike,
        operator: IntUnaryOperator = IntUnaryOperator.identity(),
    ): HTFluidResult = with(HiiragiCoreAccess.INSTANCE) {
        val fluid: HTFluidHolderLike<*> = registeredFluids.getOrThrow(prefix, material)
        val propertyMap: HTPropertyMap = materialManager.getOrEmpty(material)
        create(fluid, operator.applyAsInt(propertyMap.getDefaultFluidAmount()))
    }
}
