package hiiragi283.core.api.collection

inline fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTImmutableTable.Builder<R, C, V>.() -> Unit): HTTable<R, C, V> =
    HTImmutableTable.create(HTImmutableTable.Builder<R, C, V>().apply(builderAction).build())
