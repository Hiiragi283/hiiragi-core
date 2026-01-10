package hiiragi283.core.util

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.fluid.createFluidStack
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

object HTMoltenMetalHelper {
    @JvmStatic
    fun getMoltenMetal(holder: DataComponentHolder): HTMaterialKey? = holder.get(HCDataComponents.MATERIAL)

    @JvmStatic
    fun createFluid(material: HTMaterialLike, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack =
        createFluidStack(HCFluids.MOLTEN_METAL.get(), HCDataComponents.MATERIAL, material.asMaterialKey(), amount)

    @JvmStatic
    fun createFluid(holder: DataComponentHolder): FluidStack = getMoltenMetal(holder)?.let(::createFluid) ?: FluidStack.EMPTY

    @JvmStatic
    fun createBucket(material: HTMaterialLike): ItemStack =
        createItemStack(HCFluids.MOLTEN_METAL.getBucket(), HCDataComponents.MATERIAL, material.asMaterialKey())

    @JvmStatic
    fun createBucket(holder: DataComponentHolder): ItemStack = getMoltenMetal(holder)?.let(::createBucket) ?: ItemStack.EMPTY
}
