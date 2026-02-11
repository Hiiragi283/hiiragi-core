package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.registry.register.HTDeferredCreativeTabRegister
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items

object HCCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val COMMON: HTDeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.registerSimpleTab(
        "common",
        HCTranslation.HIIRAGI_CORE,
        HCItems.IRIDESCENT_POWDER,
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, HCItems.REGISTER.asSequence())
        // Blocks
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, HCBlocks.REGISTER.asItemSequence())
        // Fluids
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, HCFluids.REGISTER.asItemSequence())
    }

    @JvmField
    val MATERIAL: HTDeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.registerTab(
        "material",
        HCTranslation.CREATIVE_TAB_MATERIAL,
        Items.IRON_INGOT,
    ) {
        withTabsBefore(COMMON.key)
        displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
            // Items
            HTDeferredCreativeTabRegister.addToDisplay(
                parameters,
                output,
                HiiragiCoreAccess.INSTANCE.patchedMaterialContents.getAllItems(),
            )
            // Blocks
            HTDeferredCreativeTabRegister.addToDisplay(
                parameters,
                output,
                HiiragiCoreAccess.INSTANCE.patchedMaterialContents.getAllBlocks(),
            )
            // Fluids
            HTDeferredCreativeTabRegister.addToDisplay(
                parameters,
                output,
                HiiragiCoreAccess.INSTANCE
                    .patchedMaterialContents
                    .getAllMoltenFluids()
                    .map(HTFluidContent::bucketHolder),
            )
        }
    }

    @JvmField
    val EQUIPMENT: HTDeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.registerTab(
        "equipment",
        HCTranslation.CREATIVE_TAB_EQUIPMENT,
        Items.IRON_PICKAXE,
    ) {
        withTabsBefore(MATERIAL.key)
        displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
            // Items
            HTDeferredCreativeTabRegister.addToDisplay(
                parameters,
                output,
                HiiragiCoreAccess.INSTANCE.patchedMaterialContents.getAllTools(),
            )
        }
    }
}
