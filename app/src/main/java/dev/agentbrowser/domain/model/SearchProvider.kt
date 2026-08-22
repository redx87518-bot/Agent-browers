package dev.agentbrowser.domain.model

import java.net.URLEncoder

interface SearchProvider {
    val name: String
    val searchUrl: String
    fun buildSearchUrl(query: String): String
}

object GoogleSearchProvider : SearchProvider {
    override val name: String = "Google"
    override val searchUrl: String = "https://www.google.com/search?q="
    override fun buildSearchUrl(query: String): String = "$searchUrl${URLEncoder.encode(query, Charsets.UTF_8)}"
}
