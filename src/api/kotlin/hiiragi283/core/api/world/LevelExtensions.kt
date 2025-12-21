package hiiragi283.core.api.world

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

//    Position    //

/**
 * この[座標][this]を中心とし，[radius]を半径とする[三次元の範囲][AABB]を返します。
 */
fun Vec3.getRangedAABB(radius: Number): AABB = AABB(this, this).inflate(radius.toDouble())

//    Level    //

/**
 * 指定した[レベル][this]と[座標][pos]から[BlockEntity]を取得します。
 * @param BE [BlockEntity]のクラス
 * @return 指定した[座標][pos]に[BlockEntity]がない場合，または取得した[BlockEntity]が[BE]にキャストできない場合は`null`
 */
inline fun <reified BE : BlockEntity> BlockGetter.getTypedBlockEntity(pos: BlockPos): BE? = this.getBlockEntity(pos) as? BE

/**
 * 指定した[レベル][this]と[座標][pos]にブロック更新を発生させます。
 */
fun Level.sendBlockUpdated(pos: BlockPos) {
    val state: BlockState = this.getBlockState(pos)
    this.sendBlockUpdated(pos, state, state, 3)
}
