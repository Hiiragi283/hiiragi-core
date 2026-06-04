package hiiragi283.lib.material

import hiiragi283.lib.HTRegistries
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toId
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

typealias HTMaterialKey = ResourceKey<HTMaterialContents>

fun HTMaterialKey(id: Identifier): HTMaterialKey = HTRegistries.Keys.MATERIAL_CONTENTS.createKey(id)

fun HTMaterialKey(namespace: String, path: String): HTMaterialKey = HTMaterialKey(namespace.toId(path))
