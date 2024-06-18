package com.capstone.psyheart.api

import com.capstone.psyheart.model.LoginResponse
import com.capstone.psyheart.model.LogoutResponse
import com.capstone.psyheart.model.ProfileResponse
import com.capstone.psyheart.model.QuestionnaireResponse
import com.capstone.psyheart.model.RegisterResponse
import com.capstone.psyheart.model.SongCategoriesByIDResponse
import com.capstone.psyheart.model.SongCategoriesResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String?
    ): LogoutResponse

    @FormUrlEncoded
    @PUT("profiles")
    suspend fun editprofile(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("new_password") password: String,
        @Header("Authorization") token: String?
    ): ProfileResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("questionnaire")
    suspend fun questionnaire(
        @Header("Authorization") token: String?,
    ): QuestionnaireResponse

    @GET("song_categories")
    suspend fun songCategories(
        @Header("Authorization") token: String?,
    ): SongCategoriesResponse

    @GET("song_categories_by_id")
    suspend fun songCategoriesByID(
        @Header("Authorization") token: String?,
    ): SongCategoriesByIDResponse

}