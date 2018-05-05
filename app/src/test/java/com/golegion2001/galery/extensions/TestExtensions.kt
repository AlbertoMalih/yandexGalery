package com.golegion2001.galery.extensions

import org.mockito.Mockito
import java.util.*

fun <T> any(): T = Mockito.any<T>()

fun randomString() = UUID.randomUUID().toString()