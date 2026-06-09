package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCVillagerTrades
import hiiragi283.lib.data.tag.HTTagsProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.VillagerTradeTags
import net.minecraft.world.item.trading.VillagerTrade

class HCVillagerTradeTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider<VillagerTrade>(output, Registries.VILLAGER_TRADE, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        tag(VillagerTradeTags.FISHERMAN_LEVEL_5).add(HCVillagerTrades.FISHERMAN_5_EMERALD_ELDER_HEART)
    }
}
