package com.app.data.utils

import retrofit2.Response

typealias NetworkAPIInvoke<T> = suspend () -> Response<T>