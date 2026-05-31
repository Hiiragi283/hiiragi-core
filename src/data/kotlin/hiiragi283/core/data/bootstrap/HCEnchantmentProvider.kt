package hiiragi283.core.data.bootstrap

import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCEnchantments
import net.minecraft.advancements.criterion.DamageSourcePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.TagPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.EnchantmentTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EntityType
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

/**
 * @see net.minecraft.world.item.enchantment.Enchantments
 */
object HCEnchantmentProvider : RegistrySetBuilder.RegistryBootstrap<Enchantment> {
    override fun run(context: BootstrapContext<Enchantment>) {
        val enchLookup: HolderGetter<Enchantment> = context.lookup(Registries.ENCHANTMENT)
        val entityLookup: HolderGetter<EntityType<*>> = context.lookup(Registries.ENTITY_TYPE)
        val itemLookup: HolderGetter<Item> = context.lookup(Registries.ITEM)

        fun swordBuilder(): Enchantment.Builder = Enchantment
            .enchantment(
                Enchantment.definition(
                    itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                    itemLookup.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
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
            context,
            HCEnchantments.HAMMER_OF_JUSTICE,
            swordBuilder()
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(2.5f)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder
                            .entity()
                            .of(entityLookup, HiiragiCoreTags.EntityTypes.SENSITIVE_TO_HAMMER_OF_JUSTICE),
                    ),
                ),
        )
        register(
            context,
            HCEnchantments.NOISE_CANCELING,
            swordBuilder()
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(20f)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder
                            .entity()
                            .of(entityLookup, HiiragiCoreTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING),
                    ),
                ),
        )
        register(
            context,
            HCEnchantments.PURIFICATION,
            swordBuilder()
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(5f)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder
                            .entity()
                            .of(entityLookup, HiiragiCoreTags.EntityTypes.SENSITIVE_TO_PURIFICATION),
                    ),
                ),
        )
        // Armor
        register(
            context,
            HCEnchantments.SONIC_PROTECTION,
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

    //    Extensions    //

    private fun register(context: BootstrapContext<Enchantment>, key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
        context.register(key, builder.build(key.identifier()))
    }
}
