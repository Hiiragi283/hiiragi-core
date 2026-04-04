package hiiragi283.core.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDyeColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toId
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.client.util.HTFluidModelHelper
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCMenuTypes
import net.minecraft.world.level.material.FluidState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.fluid.FluidTintSource
import net.neoforged.neoforge.fluids.FluidStack
import java.awt.Color

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
class HiiragiCoreClient(eventBus: IEventBus, container: ModContainer) {
    init {
        eventBus.addListener(::registerFluidModels)
        eventBus.addListener(::registerScreens)

        HiiragiCoreAPI.LOGGER.info("Hiiragi Core Client has been loaded successfully!")
    }

    private fun registerFluidModels(event: RegisterFluidModelsEvent) {
        for ((color: HTDyeColor, content: HTFluidContent) in HCFluids.DYE) {
            HTFluidModelHelper.registerDull(event, content, color.colorObj)
        }

        HTFluidModelHelper.registerClear(event, HCFluids.EXPERIENCE, Color(0x66ff33))
        HTFluidModelHelper.register(event, HTConst.MINECRAFT.toId(HTConst.BLOCK, "honey_block_top"), HCFluids.HONEY)
        HTFluidModelHelper.registerDull(event, HCFluids.MUSHROOM_STEW, Color(0xcc9966))
        HTFluidModelHelper.registerMolten(event, HCFluids.DRAGON_BREATH, Color(0xcc66cc))
        HTFluidModelHelper.register(
            event,
            HTConst.NEOFORGE.toId(HTConst.BLOCK, "milk_still"),
            HCFluids.POTION,
            object : FluidTintSource {
                override fun color(state: FluidState): Int = -1

                override fun colorAsStack(stack: FluidStack): Int = HTPotionHelper.getPotion(stack).color
            },
            HTConst.NEOFORGE.toId(HTConst.BLOCK, "milk_flowing"),
        )
        HTFluidModelHelper.registerMolten(event, HCFluids.OMINOUS_FLUX, Color(0x003366))

        HTFluidModelHelper.registerDull(event, HCFluids.LATEX, Color(0xcccccc))
        HTFluidModelHelper.registerDull(event, HCFluids.MEAT, Color(0x993333))
    }

    private fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(HCMenuTypes.BLOCK.get(), ::HTWidgetContainerScreen)
        event.register(HCMenuTypes.ITEM.get(), ::HTWidgetContainerScreen)
    }
}
