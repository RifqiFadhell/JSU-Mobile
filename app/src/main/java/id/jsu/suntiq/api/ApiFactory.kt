package id.jsu.suntiq.api

import id.jsu.suntiq.api.response.login.LoginResponse
import id.jsu.suntiq.api.request.login.LoginRequest
import id.jsu.suntiq.api.request.register.RegisterRequest
import id.jsu.suntiq.api.request.vehicle.DetailRequest
import id.jsu.suntiq.api.response.register.RegisterResponse
import id.jsu.suntiq.api.response.register.verify.VerifyResponse
import id.jsu.suntiq.api.response.vehicle.DetailVehicleResponse
import id.jsu.suntiq.api.response.vehicle.VehicleResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiFactory {

    /*GET*/

    @GET("vehicle/sync")
    fun getVehicle(): Observable<VehicleResponse>

    /*POST*/

    @POST("login")
    fun login(@Body request: LoginRequest): Observable<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Observable<RegisterResponse>

    @POST("verification")
    fun verify(@Body requestBody: RequestBody): Observable<VerifyResponse>

    @POST("vehicle/show")
    fun detailVehicle(@Body request: DetailRequest): Observable<DetailVehicleResponse>
}

