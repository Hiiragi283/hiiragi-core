package hiiragi283.lib.material

import hiiragi283.lib.HTRegistries
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toId
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * 素材のキーを表すエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTMaterialKey = ResourceKey<HTMaterialContents>

/**
 * 新しい[HTMaterialKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun HTMaterialKey(id: Identifier): HTMaterialKey = HTRegistries.Keys.MATERIAL_CONTENTS.createKey(id)

/**
 * 新しい[HTMaterialKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun HTMaterialKey(namespace: String, path: String): HTMaterialKey = HTMaterialKey(namespace.toId(path))

/**
 * 素材の名前を取得します。
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
val HTMaterialKey.name: String get() = this.identifier().path
