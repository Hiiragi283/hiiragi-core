package hiiragi283.core.api.block.entity

import hiiragi283.core.api.serialization.component.HTComponentSerializable
import hiiragi283.core.api.serialization.value.HTValueSerializable
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [BlockEntity]で使用されるコンポーネントを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.tile.component.ITileComponent
 */
interface HTBlockEntityComponent :
    HTValueSerializable,
    HTComponentSerializable
