package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.common.item.HTAlmightyPickaxeItem
import hiiragi283.core.common.tag.HiiragiCoreTags
import hiiragi283.core.impl.registry.HTDeferredItemRegister
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.DamageResistant
import net.minecraft.world.item.component.Tool
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet
import net.neoforged.neoforge.registries.holdersets.NotHolderSet

data object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(bus: IEventBus) {
        REGISTER.register(bus)
    }

    //    Utilities    //

    //    End Game    //

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("iridescent_powder") { prop: Item.Properties ->
        prop
            .invulnerable()
            .rarity(Rarity.EPIC)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    }

    @JvmField
    val ALMIGHTY_PICKAXE: HTSimpleItemHolderLike = REGISTER.registerItem(
        "almighty_pickaxe",
        ::HTAlmightyPickaxeItem,
    ) { prop: Item.Properties ->
        prop
            .durability(2048)
            .enchantable(25)
            .invulnerable()
            .rarity(Rarity.EPIC)
            .repairable(HiiragiCoreTags.Items.ALMIGHTY_PICKAXE_MATERIALS)
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

    @JvmStatic
    private fun Item.Properties.invulnerable(): Item.Properties =
        this.delayedComponent(DataComponents.DAMAGE_RESISTANT) { provider: HolderLookup.Provider ->
            NotHolderSet(
                provider.lookupOrThrow(Registries.DAMAGE_TYPE),
                provider.getOrThrow(DamageTypeTags.BYPASSES_INVULNERABILITY),
            ).let(::DamageResistant)
        }
}
