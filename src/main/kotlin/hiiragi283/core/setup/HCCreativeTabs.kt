package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab

data object HCCreativeTabs {
    @JvmField
    val COMMON: ResourceKey<CreativeModeTab> = Registries.CREATIVE_MODE_TAB.createKey(HiiragiCoreAPI.id("common"))
}
