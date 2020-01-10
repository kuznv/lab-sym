package model.utils

import org.w3c.dom.NodeList
import kotlin.reflect.KClass

inline fun <reified T> Iterable<*>.allAreInstanceOf() = all { it is T }

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Iterable<*>.asIterableOf(): Iterable<T>? = takeIf { allAreInstanceOf<T>() } as Iterable<T>?

operator fun NodeList.iterator() = (0 until length).asSequence().map(::item)
operator fun NodeList.get(nodeName: String) = iterator().filter { it.nodeName == nodeName }

typealias DoubleRange = ClosedFloatingPointRange<Double>

val DoubleRange.length get() = endInclusive - start
val DoubleRange.middle get() = start + length / 2

fun IntRange.toDoubleRange(): ClosedFloatingPointRange<Double> = start.toDouble()..endInclusive.toDouble()

fun CharSequence?.orEmpty() = this ?: ""

inline fun <T> T?.orElse(block: () -> T): T = this ?: block()

@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.allNestedClasses(): List<KClass<out T>> =
    nestedClasses.flatMap { listOf(it) + it.allNestedClasses() } as List<KClass<out T>>