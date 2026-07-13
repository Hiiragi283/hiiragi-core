package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.TagPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.EnchantmentTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.item.enchantment.effects.AddValue
import net.minecraft.world.item.enchantment.effects.DamageImmunity
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition

data object HCEnchantments : RegistrySetBuilder.RegistryBootstrap<Enchantment> {
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

    //    Bootstrap    //

    override fun run(registry: BootstrapContext<Enchantment>) {
        val enchLookup: HolderGetter<Enchantment> = registry.lookup(Registries.ENCHANTMENT)
        val itemLookup: HolderGetter<Item> = registry.lookup(Registries.ITEM)

        fun swordBuilder(): Enchantment.Builder = Enchantment
            .enchantment(
                Enchantment.definition(
                    itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                    itemLookup.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    5,
                    5,
                    Enchantment.dynamicCost(5, 8),
                    Enchantment.dynamicCost(25, 8),
                    2,
                    EquipmentSlotGroup.MAINHAND,
                ),
            ).exclusiveWith(enchLookup.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))

        // Weapon
        register(
            registry,
            HAMMER_OF_JUSTICE,
            swordBuilder()
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(2.5f)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder
                            .entity()
                            .of(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_HAMMER_OF_JUSTICE),
                    ),
                ),
        )
        register(
            registry,
            NOISE_CANCELING,
            swordBuilder()
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(20f)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder
                            .entity()
                            .of(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING),
                    ),
                ),
        )
        register(
            registry,
            PURIFICATION,
            swordBuilder()
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(5f)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder
                            .entity()
                            .of(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_PURIFICATION),
                    ),
                ),
        )
        // Armor
        register(
            registry,
            SONIC_PROTECTION,
            Enchantment
                .enchantment(
                    Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        2,
                        1,
                        Enchantment.dynamicCost(10, 10),
                        Enchantment.dynamicCost(25, 10),
                        4,
                        EquipmentSlotGroup.ANY,
                    ),
                ).exclusiveWith(enchLookup.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE_IMMUNITY,
                    DamageImmunity.INSTANCE,
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder
                            .damageType()
                            .tag(TagPredicate.`is`(HiiragiCoreTags.DamageTypes.IS_SONIC))
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)),
                    ),
                ),
        )
    }

    @JvmStatic
    private fun register(context: BootstrapContext<Enchantment>, key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
        context.register(key, builder.build(key.location()))
    }
}
