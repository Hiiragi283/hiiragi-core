package hiiragi283.core.common.integration

import appeng.api.AECapabilities
import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.Multimap
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.pack.HTDynamicDatapack
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.common.integration.ae2.storage.HTFluidTankMEStorage
import hiiragi283.core.common.integration.immersive.HCIEIntegration
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCItems
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurio

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreIntegration : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        HTDynamicDatapack.addDomain(HCIConstants.AE2)
        HTDynamicDatapack.addDomain(HCIConstants.REPLICATION)

        if (HCIConstants.isLoaded(HCIConstants.IMMERSIVE)) {
            HCIEIntegration.init(eventBus)
        }
    }

    override fun registerCapabilities(helper: CapabilityHelper) {
        if (HCIConstants.isLoaded(HCIConstants.AE2)) {
            helper.registerBlockEntity(AECapabilities.ME_STORAGE, HCBlockEntityTypes.COPPER_BASIN.get()) { blockEntity, _ -> HTFluidTankMEStorage(blockEntity.tank, blockEntity.name) }
        }
        if (HCIConstants.isLoaded("curios")) {
            helper.registerSimpleItem(CuriosCapability.ITEM, { stack: ItemStack ->
                object : ICurio {
                    override fun getStack(): ItemStack = stack

                    override fun getAttributeModifiers(slotContext: SlotContext, id: ResourceLocation): Multimap<Holder<Attribute>, AttributeModifier> {
                        val builder: ImmutableListMultimap.Builder<Holder<Attribute>, AttributeModifier> = ImmutableListMultimap.builder()
                        stack.attributeModifiers.forEach(EquipmentSlot.OFFHAND, builder::put)
                        return builder.build()
                    }
                }
            }, HCItems.RING_OF_HYPERION)
        }
    }
}
