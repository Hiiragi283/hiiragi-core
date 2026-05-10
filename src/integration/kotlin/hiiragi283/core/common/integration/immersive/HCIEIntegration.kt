package hiiragi283.core.common.integration.immersive

import blusunrize.immersiveengineering.common.fluids.PotionFluid
import blusunrize.immersiveengineering.common.register.IEDataComponents
import blusunrize.immersiveengineering.common.register.IEFluids
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import net.minecraft.core.component.DataComponentHolder
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.MutableDataComponentHolder

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
                    override fun get(holder: DataComponentHolder): HTBottleType? = when (holder.get(IEDataComponents.POTION_BOTTLE_TYPE)) {
                        PotionFluid.PotionBottleType.REGULAR -> HTBottleType.DEFAULT
                        PotionFluid.PotionBottleType.SPLASH -> HTBottleType.SPLASH
                        PotionFluid.PotionBottleType.LINGERING -> HTBottleType.LINGERING
                        else -> null
                    }

                    override fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType) {
                        holder.set(
                            IEDataComponents.POTION_BOTTLE_TYPE,
                            when (bottleType) {
                                HTBottleType.DEFAULT -> PotionFluid.PotionBottleType.REGULAR
                                HTBottleType.SPLASH -> PotionFluid.PotionBottleType.SPLASH
                                HTBottleType.LINGERING -> PotionFluid.PotionBottleType.LINGERING
                            },
                        )
                    }
                },
            )
        }
    }
}
