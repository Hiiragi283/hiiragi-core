package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab

data object HCCreativeTabs {
    @JvmField
    val COMMON: ResourceKey<CreativeModeTab> = create("common")

    @JvmField
    val MATERIAL: ResourceKey<CreativeModeTab> = create(HTConst.MATERIAL)

    @JvmField
    val EQUIPMENT: ResourceKey<CreativeModeTab> = create("equipment")

    @JvmStatic
    private fun create(name: String) = Registries.CREATIVE_MODE_TAB.createKey(HiiragiCoreAPI.id(name))
}
