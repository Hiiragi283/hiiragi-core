package hiiragi283.lib.registry

import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey

class HTDeferredMaterialContentsRegister(namespace: String) : HTDeferredRegister<HTMaterialContents>(HTRegistries.Keys.MATERIAL_CONTENTS, namespace) {
    inline fun registerContents(name: String, primalKey: HTMaterialPartKey, builderAction: HTMaterialContents.Builder.() -> Unit): HTDeferredMaterialContents {
        val contents: HTMaterialContents = HTMaterialContents.create(HTMaterialKey.of(createId(name)), primalKey, builderAction)
        this.register(name) { _ -> contents }
        return HTDeferredMaterialContents(createId(name))
    }
}
