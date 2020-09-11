package com.rafaelrain.headbank.util

import org.koin.core.context.KoinContextHandler

inline fun <reified T> inject() = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { KoinContextHandler.get().get<T>() }
