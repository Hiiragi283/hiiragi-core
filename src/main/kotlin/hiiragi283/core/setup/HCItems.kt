package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.common.item.HTAlmightyPickaxeItem
import hiiragi283.core.common.registry.HTDeferredItemRegister
import hiiragi283.core.common.tag.HiiragiCoreTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.Tool
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet

data object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(bus: IEventBus) {
        REGISTER.register(bus)
    }

    //    End Game    //

    @JvmField
    val ALMIGHTY_PICKAXE: HTSimpleItemHolderLike = REGISTER.registerItem(
        "almighty_pickaxe",
        ::HTAlmightyPickaxeItem,
    ) { prop: Item.Properties ->
        prop
            .durability(2048)
            .repairable(HiiragiCoreTags.Items.ALMIGHTY_PICKAXE_MATERIALS)
            .enchantable(25)
            .delayedComponent(DataComponents.TOOL) { provider: HolderLookup.Provider ->
                Tool(
                    listOf(
                        Tool.Rule.deniesDrops(provider.getOrThrow(HiiragiCoreTags.Blocks.INCORRECT_FOR_ALMIGHTY_PICKAXE)),
                        Tool.Rule(
                            AnyHolderSet(provider.lookupOrThrow(Registries.BLOCK)),
                            40f.wrapOptional(),
                            true.wrapOptional(),
                        ),
                    ),
                    1f,
                    1,
                    true,
                )
            }
    }
}
