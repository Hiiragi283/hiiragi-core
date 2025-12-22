package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.common.registry.register.HTDeferredCreativeTabRegister
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items

@Suppress("DEPRECATION")
object HCCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val COMMON: HTDeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.registerSimpleTab(
        "common",
        HCTranslation.HIIRAGI_CORE,
        Items.DIAMOND_PICKAXE.builtInRegistryHolder(),
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, HCItems.REGISTER.asSequence())
        // Blocks
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, HCBlocks.REGISTER.asItemSequence())
        // Fluids
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, HCFluids.REGISTER.asItemSequence())
    }
}
