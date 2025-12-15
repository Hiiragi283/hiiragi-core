package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.registry.register.HTDeferredCreativeTabRegister
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

@Suppress("DEPRECATION")
object HCCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val MATERIAL: HTDeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.registerSimpleTab(
        "material",
        HCTranslation.CREATIVE_TAB_MATERIAL,
        Items.DIAMOND.builtInRegistryHolder(),
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        val lookup: HolderLookup.RegistryLookup<Item> = parameters.holders
            .lookupOrThrow(Registries.ITEM)
            .filterFeatures(parameters.enabledFeatures())
        val modIds: Array<String> = arrayOf(HTConst.MINECRAFT, HiiragiCoreAPI.MOD_ID)

        for (material: HCMaterial in HCMaterial.entries) {
            for (prefix: HTPrefixLike in material.getSupportedItemPrefixes()) {
                lookup
                    .get(prefix.itemTagKey(material))
                    .stream()
                    .flatMap(HolderSet.Named<Item>::stream)
                    .forEach { holder: Holder<Item> ->
                        val item: HTItemHolderLike<*> = holder.toItemLike()
                        if (item.getNamespace() in modIds) {
                            output.accept(item)
                        }
                    }
            }
        }
    }
}
