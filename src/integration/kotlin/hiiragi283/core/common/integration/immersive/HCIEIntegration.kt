package hiiragi283.core.common.integration.immersive

import blusunrize.immersiveengineering.common.fluids.PotionFluid
import blusunrize.immersiveengineering.common.register.IEDataComponents
import blusunrize.immersiveengineering.common.register.IEFluids
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.serialization.component.DataComponentGetter
import hiiragi283.core.api.serialization.component.DataComponentSetter
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

data object HCIEIntegration {
    //    Setup    //

    @JvmStatic
    internal fun init(eventBus: IEventBus) {
        eventBus.addListener(::commonSetup)
    }

    @JvmStatic
    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            HTPotionFluidManager.register(
                IEFluids.POTION.get(),
                object : HTPotionFluidManager.Handler {
                    override fun get(getter: DataComponentGetter): HTBottleType? = when (getter.get(IEDataComponents.POTION_BOTTLE_TYPE.get())) {
                        PotionFluid.PotionBottleType.REGULAR -> HTBottleType.DEFAULT
                        PotionFluid.PotionBottleType.SPLASH -> HTBottleType.SPLASH
                        PotionFluid.PotionBottleType.LINGERING -> HTBottleType.LINGERING
                        else -> null
                    }

                    override fun set(setter: DataComponentSetter, bottleType: HTBottleType) {
                        setter[IEDataComponents.POTION_BOTTLE_TYPE.get()] = when (bottleType) {
                            HTBottleType.DEFAULT -> PotionFluid.PotionBottleType.REGULAR
                            HTBottleType.SPLASH -> PotionFluid.PotionBottleType.SPLASH
                            HTBottleType.LINGERING -> PotionFluid.PotionBottleType.LINGERING
                        }
                    }
                },
            )
        }
    }
}
