package hiiragi283.core.impl.registry

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

class HTDeferredMaterial<M : HTMaterial> :
    HTBasicHolderLike<HTMaterial, M>,
    HTMaterialLike,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<HTMaterial>) : super(key)

    constructor(id: Identifier) : super(HCRegistries.Keys.MATERIAL, id)

    @Suppress("UNCHECKED_CAST")
    override fun get(): M = HCRegistries.MATERIAL.getValueOrThrow(key) as M

    override fun asMaterial(): M = get()

    override fun toString(): String = "HTDeferredMaterial(key=$key)"

    override val translationKey: String get() = get().translationKey

    override fun getText(): Text = get().getText()
}
