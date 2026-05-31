package hiiragi283.core.client

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.client.renderer.HCCopperBasinRenderer
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.HTConstants
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.fluid.HTFluidModelRegister
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.mod.HTClientMod
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.vanillaId
import java.awt.Color
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.world.level.material.FluidState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.fluid.FluidTintSource
import net.neoforged.neoforge.fluids.FluidStack

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
data object HiiragiCoreClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
    }

    override fun registerFluidModels(register: HTFluidModelRegister) {
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DyeContents) {
            register.register(content) {
                setDull()
                color.color.let(::colorTint)
            }
        }

        register.register(HCFluids.EXPERIENCE) {
            setClear()
            colorTint(Color(0x66ff33))
        }
        register.register(HCFluids.HONEY) {
            still = Material(vanillaId(HTConstants.BLOCK, "honey_block_top"), true)
            copyStillToFlowing()
        }
        register.register(HCFluids.MUSHROOM_STEW) {
            setDull()
            colorTint(Color(0xcc9966))
        }
        // Dragon Breath
        register.register(HCFluids.POTION) {
            setDull()
            tintSource = object : FluidTintSource {
                override fun color(state: FluidState): Int = -1

                override fun colorAsStack(stack: FluidStack): Int = "ff000000".hexToInt() or HTPotionHelper.getPotion(stack).color
            }
        }
        register.register(HCFluids.OMINOUS_FLUX) {
            setDull()
            colorTint(Color(0x003366))
        }

        register.register(HCFluids.LATEX) {
            setDull()
            colorTint(Color(0xcccccc))
        }
        register.register(HCFluids.MEAT) {
            setDull()
            colorTint(Color(0x993333))
        }
    }

    override fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(HCBlockEntityTypes.COPPER_BASIN.get(), ::HCCopperBasinRenderer)
    }
}
