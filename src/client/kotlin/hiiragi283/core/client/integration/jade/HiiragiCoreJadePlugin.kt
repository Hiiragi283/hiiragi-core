package hiiragi283.core.client.integration.jade

import hiiragi283.core.common.block.HTCrucibleBlock
import hiiragi283.core.common.block.entity.HTCrucibleBlockEntity
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin

@WailaPlugin
class HiiragiCoreJadePlugin : IWailaPlugin {
    override fun register(registration: IWailaCommonRegistration) {
        registration.registerBlockDataProvider(HCCrucibleJadeProvider.Server, HTCrucibleBlockEntity::class.java)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(HCCrucibleJadeProvider.Client, HTCrucibleBlock::class.java)
    }
}
