package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCEnchantments
import hiiragi283.lib.data.tag.HTTagsProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment

class HCEnchantmentTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider<Enchantment>(output, Registries.ENCHANTMENT, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        builder(EnchantmentTags.NON_TREASURE)
            .add(HCEnchantments.HAMMER_OF_JUSTICE)
            .add(HCEnchantments.NOISE_CANCELING)
            .add(HCEnchantments.PURIFICATION)
            .add(HCEnchantments.SONIC_PROTECTION)

        builder(EnchantmentTags.DAMAGE_EXCLUSIVE)
            .add(HCEnchantments.HAMMER_OF_JUSTICE)
            .add(HCEnchantments.NOISE_CANCELING)
            .add(HCEnchantments.PURIFICATION)
        builder(EnchantmentTags.ARMOR_EXCLUSIVE)
            .add(HCEnchantments.SONIC_PROTECTION)
    }
}
