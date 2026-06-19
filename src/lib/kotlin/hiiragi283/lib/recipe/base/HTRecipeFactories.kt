package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTBiRecipeFactory
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.HTTriRecipeFactory
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * Hiiragi Seriesで使用される[HTRecipeFactory]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipeFactories {
    //    Single Input    //

    /**
     * 1種類の液体から完成品を作る[HTRecipeFactory]の拡張インターフェースです。
     */
    fun interface SingleFluidTo<OUTPUT : Any> : HTRecipeFactory<FluidInstance, OUTPUT>

    /**
     * 1種類のアイテムから完成品を作る[HTRecipeFactory]の拡張インターフェースです。
     */
    fun interface SingleItemTo<OUTPUT : Any> : HTRecipeFactory<ItemInstance, OUTPUT>

    //    Double Input    //

    /**
     * 1種類のアイテムと液体から完成品を作る[HTBiRecipeFactory]の拡張インターフェースです。
     */
    fun interface ItemAndFluid<OUTPUT : Any> : HTBiRecipeFactory<ItemInstance, FluidInstance, OUTPUT>

    /**
     * 2種類のアイテムから完成品を作る[HTBiRecipeFactory]の拡張インターフェースです。
     */
    fun interface DoubleItem<OUTPUT : Any> : HTBiRecipeFactory<ItemInstance, ItemInstance, OUTPUT>

    //    Triple Input    //

    /**
     * 1種類のアイテムと2種類の液体から完成品を作る[HTTriRecipeFactory]の拡張インターフェースです。
     */
    fun interface ItemAndDoubleFluid<OUTPUT : Any> : HTTriRecipeFactory<ItemInstance, FluidInstance, FluidInstance, OUTPUT>

    /**
     * 2種類のアイテムと1種類の液体から完成品を作る[HTTriRecipeFactory]の拡張インターフェースです。
     */
    fun interface DoubleItemAndFluid<OUTPUT : Any> : HTTriRecipeFactory<ItemInstance, ItemInstance, FluidInstance, OUTPUT>

    /**
     * 3種類のアイテムから完成品を作る[HTTriRecipeFactory]の拡張インターフェースです。
     */
    fun interface TripleItem<OUTPUT : Any> : HTTriRecipeFactory<ItemInstance, ItemInstance, ItemInstance, OUTPUT>
}
