package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.prefix.HTMaterialTable
import hiiragi283.core.common.material.CommonMaterialPrefixes
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import net.neoforged.bus.api.IEventBus

object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTSimpleDeferredItem> = buildTable {
        for (material: HCMaterial in HCMaterial.entries) {
            for (prefix: CommonMaterialPrefixes in material.getItemPrefixesToGenerate()) {
                this.put(
                    prefix.asMaterialPrefix(),
                    material.asMaterialKey(),
                    REGISTER.registerSimpleItem(material.createPath(prefix)),
                )
            }
        }
    }.let(::HTMaterialTable)
}
