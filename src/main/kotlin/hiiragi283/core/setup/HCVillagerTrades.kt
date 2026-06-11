package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.HTVillagerTradeBuilder
import hiiragi283.lib.registry.createKey
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.trading.TradeCost
import net.minecraft.world.item.trading.VillagerTrade

/**
 * @see net.minecraft.world.item.trading.VillagerTrades
 */
data object HCVillagerTrades : RegistrySetBuilder.RegistryBootstrap<VillagerTrade> {
    @JvmStatic
    fun create(profession: String, level: Int, name: String): ResourceKey<VillagerTrade> = Registries.VILLAGER_TRADE.createKey(HiiragiCoreAPI.id("$profession/$level/$name"))

    @JvmField
    val FISHERMAN_5_EMERALD_ELDER_HEART: ResourceKey<VillagerTrade> = create("fisherman", 5, "emerald_elder_heart")

    //    Bootstrap    //

    override fun run(registry: BootstrapContext<VillagerTrade>) {
        registry.register(
            FISHERMAN_5_EMERALD_ELDER_HEART,
            HTVillagerTradeBuilder.build {
                wants = TradeCost(Items.EMERALD, 32)
                gives { +HCItems.ELDER_HEART }
                maxUses = 1
                xp = 100
            },
        )
    }
}
