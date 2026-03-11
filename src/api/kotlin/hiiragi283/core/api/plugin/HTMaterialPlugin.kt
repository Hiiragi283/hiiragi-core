package hiiragi283.core.api.plugin

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * 素材の登録を行うプラグインを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
interface HTMaterialPlugin : HTIdLike {
    /**
     * プラグインの優先度を取得します。
     */
    val priority: Int

    //    Part    //

    /**
     * 新規で[部品][HTPart]を登録します。
     */
    fun registerPart(registrar: PartRegistrar) {}

    fun interface PartRegistrar {
        fun register(name: String, idPattern: String, properties: HTPropertyMap): HTPartLike

        fun register(name: String, idPattern: String, builderAction: HTPropertyMap.Mutable.() -> Unit): HTPartLike =
            register(name, idPattern, HTBasicPropertyMap.Mutable().apply(builderAction))
    }

    //    Material    //

    /**
     * 既存の[ブロック][Block]を登録します。
     */
    fun registerExistingBlock(consumer: BlockConsumer) {}

    fun interface BlockConsumer {
        fun accept(part: HTPartLike, material: HTMaterialKey, holder: HTBlockHolderLike<*>)

        fun accept(part: HTPartLike, material: HTMaterialKey, holder: Holder<Block>) {
            this.accept(part, material, holder.toLike())
        }
    }

    /**
     * 既存の[アイテム][Item]を登録します。
     */
    fun registerExistingItem(consumer: ItemConsumer) {}

    fun interface ItemConsumer {
        fun accept(part: HTPartLike, material: HTMaterialKey, holder: HTItemHolderLike<*>)

        fun accept(part: HTPartLike, material: HTMaterialKey, holder: Holder<Item>) {
            this.accept(part, material, holder.toLike().toItemLike())
        }
    }

    /**
     * 既存の[道具][Item]を登録します。
     */
    fun registerExistingTool(consumer: ToolConsumer) {}

    fun interface ToolConsumer {
        fun accept(toolType: HTToolType, material: HTMaterialKey, holder: HTItemHolderLike<*>)

        fun accept(toolType: HTToolType, material: HTMaterialKey, holder: Holder<Item>) {
            this.accept(toolType, material, holder.toLike().toItemLike())
        }
    }

    /**
     * 素材のプロパティを編集します。
     */
    fun modifyMaterial(provider: MaterialProvider) {}

    fun interface MaterialProvider {
        fun getBuilder(key: HTMaterialKey): HTPropertyMap.Mutable
    }
}
