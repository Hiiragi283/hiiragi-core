package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.common.registry.HTDeferredCreativeTabRegister
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items

object HCCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val COMMON: HTSimpleHolderLike<CreativeModeTab> = REGISTER.registerSimpleTab(
        "common",
        HCTranslation.HIIRAGI_CORE,
        Items.APPLE.toItemLike(),
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        // HTDeferredCreativeTabRegister.addToDisplay(parameters, output, items = HCItems.REGISTER.asItemSequence())
        // Blocks
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, items = HCBlocks.REGISTER.asItemSequence())
        // Fluids
        // HTDeferredCreativeTabRegister.addToDisplay(parameters, output, items = HCFluids.REGISTER.asItemSequence())
    }
}
