package hiiragi283.lib.registry

import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.resource.SupplierWithId

class HTDeferredMaterialContentsRegister(namespace: String) : HTDeferredRegister<HTMaterialContents>(HTRegistries.Keys.MATERIAL_CONTENTS, namespace) {
    inline fun registerContents(name: String, primalKey: HTMaterialPartKey, builderAction: HTMaterialContents.Builder.() -> Unit): SupplierWithId<HTMaterialContents> {
        val contents: HTMaterialContents = HTMaterialContents.create(primalKey, builderAction)
        return this.register(name) { _ -> contents }
    }
}
