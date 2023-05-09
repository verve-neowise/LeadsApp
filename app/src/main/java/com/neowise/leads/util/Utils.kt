package com.neowise.leads.util

import com.apollographql.apollo3.api.Optional


fun Any?.isNotNull(): Boolean {
    return this != null
}

fun <T> T?.optional(): Optional<T?> {
    return Optional.present(this)
}

fun <T, R> T.mapTo(transform: (T) -> R): R = transform(this)
