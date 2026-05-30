package hiiragi283.core.data

import hiiragi283.core.api.HiiragiCoreAPI
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.theillusivec4.curios.api.CuriosDataProvider

class HCCuriosDataProvider(helper: ExistingFileHelper, output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : CuriosDataProvider(HiiragiCoreAPI.MOD_ID, output, helper, registries) {
    override fun generate(registries: HolderLookup.Provider, fileHelper: ExistingFileHelper) {
        this.createEntities("player")
            .addPlayer()
            .addSlots("ring")
    }
}
