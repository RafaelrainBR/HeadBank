package com.rafaelrain.headbank.javalinbackend.util

import org.koin.core.context.GlobalContext

inline fun <reified T : Any> inject() = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { GlobalContext.get().get<T>() }
