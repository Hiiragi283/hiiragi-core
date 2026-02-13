package hiiragi283.core.util

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResourcePair
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.storage.resource.HTResourceView
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
object HTShapelessRecipeHelper {
    //    Input    //

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromViews")
    @JvmStatic
    fun <T : HTResourceType<*>> createMap(views: Iterable<HTResourceView<T>>): Map<T, Int> =
        views.fold(hashMapOf()) { map: HashMap<T, Int>, view: HTResourceView<T> ->
            val resource: T = view.getResource() ?: return@fold map
            map[resource] = (map[resource] ?: 0) + view.getAmount()
            map
        }

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromItems")
    @JvmStatic
    fun createMap(stacks: Iterable<ItemStack>): Map<HTItemResourceType, Int> =
        stacks.fold(hashMapOf()) { map: HashMap<HTItemResourceType, Int>, stack: ItemStack ->
            val (resource: HTItemResourceType, amount: Int) = stack.toResourcePair() ?: return@fold map
            map[resource] = (map[resource] ?: 0) + amount
            map
        }

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromFluids")
    @JvmStatic
    fun createMap(stacks: Iterable<FluidStack>): Map<HTFluidResourceType, Int> =
        stacks.fold(hashMapOf()) { map: HashMap<HTFluidResourceType, Int>, stack: FluidStack ->
            val (resource: HTFluidResourceType, amount: Int) = stack.toResourcePair() ?: return@fold map
            map[resource] = (map[resource] ?: 0) + amount
            map
        }

    //    Match    //

    /**
     * 指定した[ingredients]に[stacks]が不定形で一致するか判定します。
     * @param T [HTResourceType]を実装したクラス
     * @param I [HTIngredient]を実装したクラス
     * @return 消費される[リソース][T]と消費する量のマップ
     */
    @JvmStatic
    fun <T : HTResourceType<*>, I : HTIngredient<*, T>> shapelessMatch(ingredients: List<I>, stacks: Map<T, Int>): Map<T, Int> {
        val stacks1: MutableMap<T, Int> = stacks.toMutableMap()

        var count = 0
        val resultMap: MutableMap<T, Int> = hashMapOf()

        ing@for (ingredient: I in ingredients) {
            stack@for ((resource: T, amount: Int) in stacks1) {
                if (ingredient.test(resource, amount)) {
                    resultMap[resource] = (resultMap[resource] ?: 0) + ingredient.amount
                    stacks1.remove(resource)
                    count++
                    continue@ing
                }
            }
            return mapOf()
        }
        if (count != ingredients.size) return mapOf()
        return resultMap
    }

    @JvmStatic
    fun <T : HTResourceType<*>, I : HTIngredient<*, T>> shapelessMatch(
        ingredients: List<I>,
        views: Iterable<HTResourceView<T>>,
    ): Map<T, Int> = shapelessMatch(ingredients, createMap(views))

    /**
     * [HTItemResourceType]向けのメソッドです。
     */
    @JvmName("shapelessMatchItems")
    @JvmStatic
    fun shapelessMatch(ingredients: List<HTItemIngredient>, stacks: Iterable<ItemStack>): Map<HTItemResourceType, Int> =
        shapelessMatch(ingredients, createMap(stacks))

    /**
     * [HTFluidResourceType]向けのメソッドです。
     */
    @JvmName("shapelessMatchFluids")
    @JvmStatic
    fun shapelessMatch(ingredients: List<HTFluidIngredient>, stacks: Iterable<FluidStack>): Map<HTFluidResourceType, Int> =
        shapelessMatch(ingredients, createMap(stacks))

    //    Consume    //

    /**
     * 指定した[ingredients]に基づいて[slots]からリソースを消費します。
     * @param T [HTResourceType]を実装したクラス
     * @param I [HTIngredient]を実装したクラス
     * @return すべての材料に対して消費が行われた場合は`true`
     */
    @JvmStatic
    fun <T : HTResourceType<*>, I : HTIngredient<*, T>> shapelessConsume(
        ingredients: List<I>,
        slots: Iterable<HTResourceSlot<T>>,
    ): Boolean {
        val resultMap: Map<T, Int> = shapelessMatch(ingredients, slots)
        if (resultMap.isEmpty()) return false

        val slots1: MutableList<HTResourceSlot<T>> = slots.toMutableList()
        val resultMap1: MutableMap<T, Int> = resultMap.toMutableMap()

        val iterator: MutableIterator<MutableMap.MutableEntry<T, Int>> = resultMap1.iterator()
        while (iterator.hasNext()) {
            val entry: MutableMap.MutableEntry<T, Int> = iterator.next()
            val (resource: T, amount: Int) = entry
            stack@for (slot: HTResourceSlot<T> in slots1) {
                val extracted: Int = slot.extract(resource, amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                if (extracted < amount) {
                    entry.setValue(amount - extracted)
                } else {
                    iterator.remove()
                    slots1.remove(slot)
                    break@stack
                }
            }
        }
        return resultMap1.isEmpty()
    }
}
