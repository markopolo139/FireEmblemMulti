package pl.ms.fire.emblem.web.utils

import javax.servlet.http.HttpServletRequest

fun getServerAddress(request: HttpServletRequest): String =
    request.requestURL.toString().replace(request.requestURI, "")