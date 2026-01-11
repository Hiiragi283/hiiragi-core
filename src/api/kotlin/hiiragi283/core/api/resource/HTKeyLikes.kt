package hiiragi283.core.api.resource

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.fluids.FluidStack

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
