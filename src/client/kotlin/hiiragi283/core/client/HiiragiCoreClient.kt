package hiiragi283.core.client

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.HTConstants
import hiiragi283.lib.mod.HTClientMod
import hiiragi283.lib.fluid.HTFluidModelRegister
import hiiragi283.lib.resource.toId
import net.minecraft.client.resources.model.sprite.Material
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
data object HiiragiCoreClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
    }

    override fun registerFluidModels(register: HTFluidModelRegister) {
        register.register(HCFluids.HONEY) {
            still = Material(HTConstants.MINECRAFT.toId(HTConstants.BLOCK, "honey_block_top"), true)
            copyStillToFlowing()
        }
    }
}
