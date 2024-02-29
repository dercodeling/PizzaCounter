package de.dercodeling.pizzacounter.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

const val repo = "/repos/dercodeling/PizzaCounter"

interface GithubApi {
    @GET("$repo/releases/latest")
    suspend fun getLatestRelease(): Response<Release>
    @GET("$repo/releases/tags/{tag}")
    suspend fun getReleaseByTag(@Path("tag") tag: String): Response<Release>
}