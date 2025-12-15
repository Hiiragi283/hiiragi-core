package hiiragi283.core.common.event

import hiiragi283.core.HiiragiCore
import hiiragi283.core.api.material.HTMaterialDefinitionEvent
import hiiragi283.core.api.material.addDefaultPrefix
import hiiragi283.core.common.material.HCMaterial
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber
object HCMaterialEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun gatherDefinition(event: HTMaterialDefinitionEvent) {
        event.modify(HCMaterial.entries) { material: HCMaterial ->
            addDefaultPrefix(material.basePrefix)
            HiiragiCore.LOGGER.debug("Registered definition for ${material.asMaterialName()}")
        }
    }
}
