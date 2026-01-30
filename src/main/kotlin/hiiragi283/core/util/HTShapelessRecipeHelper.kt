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
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
object HTShapelessRecipeHelper {
    //    Match    //

    /**
     * 指定した[ingredients]に[stacks]が不定形で一致するか判定します。
     * @param T [HTResourceType]を実装したクラス
     * @param I [HTIngredient]を実装したクラス
     * @return 消費される[リソース][T]と消費する量のマップ
     */
    @JvmStatic
    fun <T : HTResourceType<*>, I : HTIngredient<*, T>> shapelessMatch(ingredients: List<I>, stacks: Map<T, Int>): Map<T, Int> {
        val ingredients1: MutableList<I> = ingredients.toMutableList()
        val stacks1: MutableMap<T, Int> = stacks.toMutableMap()
        val resultMap: MutableMap<T, Int> = hashMapOf()

        val iterator: MutableIterator<I> = ingredients1.iterator()
        while (iterator.hasNext()) {
            val ingredient: I = iterator.next()
            stack@for ((resource: T, amount: Int) in stacks1) {
                if (ingredient.test(resource, amount)) {
                    resultMap[resource] = (resultMap[resource] ?: 0) + ingredient.getRequiredAmount()
                    iterator.remove()
                    stacks1.remove(resource)
                    break@stack
                }
                return mapOf()
            }
        }
        if (!ingredients1.isEmpty()) return mapOf()
        return resultMap
    }

    @JvmStatic
    fun <T : HTResourceType<*>, I : HTIngredient<*, T>> shapelessMatch(
        ingredients: List<I>,
        slots: Iterable<HTResourceSlot<T>>,
    ): Map<T, Int> {
        val stackMap: Map<T, Int> = slots
            .fold(hashMapOf()) { map: HashMap<T, Int>, slot: HTResourceSlot<T> ->
                val resource: T = slot.getResource() ?: return@fold map
                map[resource] = (map[resource] ?: 0) + slot.getAmount()
                map
            }
        return shapelessMatch(ingredients, stackMap)
    }

    /**
     * [HTItemResourceType]向けのメソッドです。
     */
    @JvmStatic
    fun shapelessMatch(ingredients: List<HTItemIngredient>, stacks: Iterable<ItemStack>): Map<HTItemResourceType, Int> {
        val stackMap: Map<HTItemResourceType, Int> = stacks
            .fold(hashMapOf()) { map: HashMap<HTItemResourceType, Int>, stack: ItemStack ->
                val (resource: HTItemResourceType, amount: Int) = stack.toResourcePair() ?: return@fold map
                map[resource] = (map[resource] ?: 0) + amount
                map
            }
        return shapelessMatch(ingredients, stackMap)
    }

    /**
     * [HTFluidResourceType]向けのメソッドです。
     */
    @JvmStatic
    fun shapelessMatch(ingredients: List<HTFluidIngredient>, stacks: Iterable<FluidStack>): Map<HTFluidResourceType, Int> {
        val stackMap: Map<HTFluidResourceType, Int> = stacks
            .fold(hashMapOf()) { map: HashMap<HTFluidResourceType, Int>, stack: FluidStack ->
                val (resource: HTFluidResourceType, amount: Int) = stack.toResourcePair() ?: return@fold map
                map[resource] = (map[resource] ?: 0) + amount
                map
            }
        return shapelessMatch(ingredients, stackMap)
    }

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
