package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [ResourceKey]を保持する[HTIdLike]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTHolderLike
 */
fun interface HTKeyLike<T : Any> : HTIdLike {
    /**
     * 保持している[ResourceKey]を返します。
     */
    fun getResourceKey(): ResourceKey<T>

    /**
     * 指定した[key]が保持している[ResourceKey]と一致するか判定します。
     * @since 0.6.0
     */
    fun isOf(key: ResourceKey<T>): Boolean = key == getResourceKey()

    /**
     * 指定した[other]と保持している[ResourceKey]が一致するか判定します。
     * @since 0.6.0
     */
    fun isOf(other: HTKeyLike<T>): Boolean = isOf(other.getResourceKey())

    override fun getId(): ResourceLocation = getResourceKey().location()

    /**
     * [Holder]を保持する[HTKeyLike]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     * @see HTHolderLike.HolderDelegate
     */
    fun interface HolderDelegate<T : Any> : HTKeyLike<T> {
        /**
         * 保持している[Holder]を返します。
         */
        fun getHolder(): Holder<T>

        /**
         * 指定した[value]が保持している[Holder]の値と一致するか判定します。
         * @since 0.6.0
         */
        fun isOf(value: T): Boolean = getHolder().value() == value

        /**
         * 指定した[tagKey]が保持している[Holder]に含まれるか判定します。
         * @since 0.6.0
         */
        fun isOf(tagKey: TagKey<T>): Boolean = getHolder().`is`(tagKey)

        /**
         * 指定した[holder]が保持している[Holder]と一致するか判定します。
         * @since 0.6.0
         */
        @Suppress("DEPRECATION")
        fun isOf(holder: Holder<T>): Boolean = getHolder().`is`(holder)

        /**
         * 指定した[holderSet]に保持している[Holder]が含まれるか判定します。
         * @since 0.6.0
         */
        fun isOf(holderSet: HolderSet<T>): Boolean = holderSet.contains(getHolder())

        override fun getResourceKey(): ResourceKey<T> = getHolder().unwrapKey().orElseThrow()
    }
}

//    Extensions    //

// Block

/**
 * 指定した[state]が保持しているブロックと一致するか判定します。
 * @since 0.6.0
 */
fun HTKeyLike.HolderDelegate<Block>.isOf(state: BlockState): Boolean = this.isOf(state.blockHolder)

// Entity

/**
 * 指定した[entity]が保持しているエンティティのタイプと一致するか判定します。
 * @since 0.6.0
 */
fun HTKeyLike.HolderDelegate<EntityType<*>>.isOf(entity: Entity): Boolean = this.isOf(entity.type)

// Fluid

/**
 * 指定した[state]が保持している液体と一致するか判定します。
 * @since 0.6.0
 */
fun HTKeyLike.HolderDelegate<Fluid>.isOf(state: FluidState): Boolean = this.isOf(state.holder())

/**
 * 指定した[stack]が保持している液体と一致するか判定します。
 * @since 0.6.0
 */
fun HTKeyLike.HolderDelegate<Fluid>.isOf(stack: FluidStack): Boolean = this.isOf(stack.fluidHolder)

// Item

/**
 * 指定した[stack]が保持しているアイテムと一致するか判定します。
 * @since 0.6.0
 */
fun HTKeyLike.HolderDelegate<Item>.isOf(stack: ItemStack): Boolean = this.isOf(stack.itemHolder)
