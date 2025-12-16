package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.item.HTEquipmentMaterial
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.item.HTHammerItem
import hiiragi283.core.common.item.HTToolType
import hiiragi283.core.common.item.VanillaEquipmentMaterial
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus

object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Materials   //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredItem> = buildTable {
        for (material: HCMaterial in HCMaterial.entries) {
            for (prefix: HTMaterialPrefix in material.getItemPrefixesToGenerate()) {
                this.put(
                    prefix.asMaterialPrefix(),
                    material.asMaterialKey(),
                    REGISTER.registerSimpleItem(material.createPath(prefix)),
                )
            }
        }
    }.let(::HTMaterialTable)

    //    Tools   //

    @JvmStatic
    val TOOLS: HTMaterialTable<HTToolType<*>, HTDeferredItem<out Item>> = buildTable {
        fun register(toolType: HTToolType<*>, material: HTEquipmentMaterial) {
            this.put(toolType, material.asMaterialKey(), toolType.createItem(REGISTER, material))
        }

        // Hammer
        VanillaEquipmentMaterial.entries.forEach(::register.partially1(HTHammerItem.ToolType))
    }.let(::HTMaterialTable)
}
