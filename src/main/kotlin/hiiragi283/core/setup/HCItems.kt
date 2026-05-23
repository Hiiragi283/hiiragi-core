package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.item.HTPotionBucketItem
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.transfer.access.ItemAccess

data object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyComponents)
        eventBus.addListener(::registerCapabilities)
    }

    //    Resources    //

    @JvmField
    val NETHERITE_NUGGET: HTSimpleDeferredItem = REGISTER.registerSimpleItem("netherite_nugget") { it.fireResistant() }

    //    Event    //

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
    }

    @JvmStatic
    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.Fluid.ITEM,
            { _, access: ItemAccess -> HTPotionBucketItem.BucketHandler(access) },
            HCFluids.POTION.bucketHolder.get(),
        )
    }
}
