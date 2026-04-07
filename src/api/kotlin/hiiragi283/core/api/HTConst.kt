package hiiragi283.core.api

import net.minecraft.client.renderer.LightTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.Item
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion

/**
 * Hiiragi Coreとそれを前提とするmodで使用される定数を集めたクラスです。
 */
data object HTConst {
    const val ZERO_CELSIUS = 273
    const val STANDARD_TEMP = 300

    //    GUI    //

    /**
     * ブロックやアイテムのテクスチャアトラスの[ResourceLocation]
     */
    @JvmField
    val BLOCK_ATLAS: ResourceLocation = InventoryMenu.BLOCK_ATLAS

    const val FULL_BRIGHT: Int = LightTexture.FULL_BRIGHT

    const val TEXTURES = "textures"
    const val GUI = "gui"

    //    Fluid    //

    /**
     * 液体におけるデフォルトの数量
     */
    const val DEFAULT_FLUID_AMOUNT: Int = FluidType.BUCKET_VOLUME

    /**
     * 1インゴットあたりの液体量
     */
    const val INGOT_AMOUNT = 90

    //    Item    //

    /**
     * アイテムにおけるデフォルトの最大個数
     */
    const val ABSOLUTE_MAX_STACK_SIZE: Int = Item.ABSOLUTE_MAX_STACK_SIZE

    //    Mod ID    //

    /**
     * MinecraftのMOD ID
     */
    const val MINECRAFT = "minecraft"

    /**
     * NeoForgeのMOD ID
     */
    const val NEOFORGE: String = NeoForgeVersion.MOD_ID

    /**
     * 共通タグで使用されるID
     */
    const val COMMON = "c"

    @JvmStatic
    fun getBuiltInIdSet(modId: String): Set<String> = setOf(MINECRAFT, NEOFORGE, COMMON, modId)

    //    Serialization    //

    const val OWNER = "owner"
    const val BLOCK = "block"

    const val ITEM = "item"
    const val SLOT = "slot"
    const val ITEMS = "items"

    const val FLUID = "fluid"
    const val TANK = "tank"
    const val FLUIDS = "fluids"

    const val AMOUNT = "amount"
    const val CAPACITY = "capacity"
    const val BATTERIES = "batteries"

    const val ID = "id"
    const val TAG = "tag"
    const val COUNT = "count"
    const val COMPONENTS = "components"

    const val COMPLETED_RECIPE = "CompletedRecipe"
    const val PREVENT_ITEM_MAGNET = "PreventRemoteMovement"

    //    Recipes    //

    // Vanilla
    const val BLASTING = "blasting"
    const val SHAPED = "shaped"
    const val SHAPELESS = "shapeless"
    const val SMELTING = "smelting"
    const val SMITHING = "smithing"
    const val SMOKING = "smoking"

    // Hiiragi Core
    const val CHARGING = "charging"
    const val CRUSHING = "crushing"
    const val EXPLODING = "exploding"
    const val FORGING = "forging"
    const val MELTING = "melting"

    const val TANK_INTERACTION = "tank_interaction"

    // Serialization
    const val ENERGY = "energy"
    const val TIME = "time"

    const val INGREDIENT = "ingredient"
    const val ITEM_INGREDIENT = "item_ingredient"
    const val FLUID_INGREDIENT = "fluid_ingredient"

    const val CATALYST = "catalyst"

    const val RESULT = "result"
    const val RESULTS = "results"
    const val ITEM_RESULT = "item_result"
    const val FLUID_RESULT = "fluid_result"

    const val CHANCE = "chance"
    const val EXTRA_RESULT = "extra_result"
}
