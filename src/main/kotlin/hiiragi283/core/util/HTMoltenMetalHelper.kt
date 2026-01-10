package hiiragi283.core.util

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.attribute.HTMoltenMetalMaterialAttribute
import hiiragi283.core.api.material.get
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

object HTMoltenMetalHelper {
    @JvmStatic
    fun getMoltenMetal(holder: DataComponentHolder): HTMaterialKey? = holder.get(HCDataComponents.MATERIAL)

    @JvmStatic
    fun isEnabled(definition: HTMaterialDefinition, allowCustom: Boolean): Boolean {
        val attribute: HTMoltenMetalMaterialAttribute = definition.get<HTMoltenMetalMaterialAttribute>() ?: return false
        return when {
            allowCustom -> attribute.enabled
            else -> attribute.enabled && attribute.custom == null
        }
    }

    @JvmStatic
    fun createResource(key: HTMaterialKey, definition: HTMaterialDefinition): HTFluidResourceType = definition
        .get<HTMoltenMetalMaterialAttribute>()
        ?.custom
        ?.toResource()
        ?: HCFluids.MOLTEN_METAL.toResource(buildDataPatch { set(HCDataComponents.MATERIAL, key) })

    @JvmStatic
    fun createFluid(key: HTMaterialKey, definition: HTMaterialDefinition, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack =
        createResource(key, definition).toStack(amount)

    @JvmStatic
    fun createFluid(material: HTMaterialLike, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack =
        createFluid(material.asMaterialKey(), HTMaterialManager.INSTANCE.getOrEmpty(material), amount)

    @JvmStatic
    fun createFluid(holder: DataComponentHolder): FluidStack = getMoltenMetal(holder)?.let(::createFluid) ?: FluidStack.EMPTY

    @JvmStatic
    fun createBucket(key: HTMaterialKey, definition: HTMaterialDefinition): ItemStack = definition
        .get<HTMoltenMetalMaterialAttribute>()
        ?.custom
        ?.getBucket()
        ?.let(::ItemStack)
        ?: createItemStack(HCFluids.MOLTEN_METAL.getBucket(), HCDataComponents.MATERIAL, key)

    @JvmStatic
    fun createBucket(material: HTMaterialLike): ItemStack =
        createBucket(material.asMaterialKey(), HTMaterialManager.INSTANCE.getOrEmpty(material))

    @JvmStatic
    fun createBucket(holder: DataComponentHolder): ItemStack = getMoltenMetal(holder)?.let(::createBucket) ?: ItemStack.EMPTY
}
