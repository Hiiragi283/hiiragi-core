package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

data object HCEnchantments {
    @JvmStatic
    fun create(name: String): ResourceKey<Enchantment> = Registries.ENCHANTMENT.createKey(HiiragiCoreAPI.id(name))

    //    Weapon    //

    @JvmField
    val HAMMER_OF_JUSTICE: ResourceKey<Enchantment> = create("hammer_of_justice")

    @JvmField
    val NOISE_CANCELING: ResourceKey<Enchantment> = create("noise_canceling")

    @JvmField
    val PURIFICATION: ResourceKey<Enchantment> = create("purification")

    //    Armor    //

    @JvmField
    val SONIC_PROTECTION: ResourceKey<Enchantment> = create("sonic_protection")
}
