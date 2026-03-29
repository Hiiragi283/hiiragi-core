package hiiragi283.core.api.block.entity

import hiiragi283.core.api.serialization.HTComponentSerializable
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.common.util.ValueIOSerializable

/**
 * [BlockEntity]で使用されるコンポーネントを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.tile.component.ITileComponent
 */
interface HTBlockEntityComponent :
    ValueIOSerializable,
    HTComponentSerializable
