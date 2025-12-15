package hiiragi283.core.api.data.advancement

import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.common.data.ExistingFileHelper

abstract class HTAdvancementGenerator {
    protected lateinit var output: HTAdvancementOutput

    fun generate(provider: HolderLookup.Provider, output: HTAdvancementOutput, helper: ExistingFileHelper) {
        this.output = output
        generate(provider)
    }

    protected abstract fun generate(registries: HolderLookup.Provider)

    //    Extension    //

    protected inline fun root(key: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
        HTAdvancementBuilder.root().apply(builderAction).save(output, key)
    }

    protected inline fun child(key: HTAdvancementKey, parent: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
        HTAdvancementBuilder.child(parent).apply(builderAction).save(output, key)
    }
}
