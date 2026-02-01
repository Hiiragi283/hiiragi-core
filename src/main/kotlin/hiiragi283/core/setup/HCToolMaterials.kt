package hiiragi283.core.setup

import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
enum class HCToolMaterials(
    private val axeDamage: Float,
    private val axeAttackSpeed: Float,
    private val enchantmentValue: Int,
    private val ingredient: TagKey<Item>,
    private val durability: Int,
    private val miningSpeed: Float,
    private val swordDamage: Float,
    private val incorrectTag: TagKey<Block>,
) : HTToolMaterial {
    // Common
    STEEL(
        7f,
        -3f,
        16,
        CommonTagPrefixes.INGOT,
        CommonMaterialKeys.STEEL,
        512,
        8f,
        3f,
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
    ),
    BRONZE(
        7f,
        -3f,
        10,
        CommonTagPrefixes.INGOT,
        CommonMaterialKeys.BRONZE,
        375,
        7f,
        2f,
        BlockTags.INCORRECT_FOR_IRON_TOOL,
    ),

    // Hiiragi Core
    ANCIENT_METAL(
        7f,
        -2.9f,
        18,
        CommonTagPrefixes.INGOT,
        HCMaterialKeys.ANCIENT_METAL,
        2048,
        12f,
        8f,
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
    ),
    AZURE_STEEL(
        7f,
        -3f,
        16,
        CommonTagPrefixes.INGOT,
        HCMaterialKeys.AZURE_STEEL,
        512,
        8f,
        3f,
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
    ),
    ;

    constructor(
        axeDamage: Float,
        axeAttackSpeed: Float,
        enchantmentValue: Int,
        prefix: HTTagPrefix,
        material: HTMaterialLike,
        durability: Int,
        miningSpeed: Float,
        swordDamage: Float,
        incorrectTag: TagKey<Block>,
    ) : this(
        axeDamage,
        axeAttackSpeed,
        enchantmentValue,
        prefix.itemTagKey(material),
        durability,
        miningSpeed,
        swordDamage,
        incorrectTag,
    )

    override fun getAxeDamage(): Float = axeDamage

    override fun getAxeAttackSpeed(): Float = axeAttackSpeed

    override fun getEnchantmentValue(): Int = enchantmentValue

    override fun getRepairIngredient(): Ingredient = Ingredient.of(ingredient)

    override fun getUses(): Int = durability

    override fun getSpeed(): Float = miningSpeed

    override fun getAttackDamageBonus(): Float = swordDamage

    override fun getIncorrectBlocksForDrops(): TagKey<Block> = incorrectTag
}
