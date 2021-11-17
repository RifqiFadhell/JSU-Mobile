package id.jsu.suntiq.api

import id.jsu.suntiq.api.request.CompleteRequest
import id.jsu.suntiq.api.request.vehicle.cancel.CancelRequest
import id.jsu.suntiq.api.request.location.LocationsRequest
import id.jsu.suntiq.api.response.login.LoginResponse
import id.jsu.suntiq.api.request.login.LoginRequest
import id.jsu.suntiq.api.request.register.RegisterRequest
import id.jsu.suntiq.api.request.vehicle.detail.DetailRequest
import id.jsu.suntiq.api.response.CompleteResponse
import id.jsu.suntiq.api.response.location.LocationResponse
import id.jsu.suntiq.api.response.register.RegisterResponse
import id.jsu.suntiq.api.response.register.verify.VerifyResponse
import id.jsu.suntiq.api.response.vehicle.DetailVehicleResponse
import id.jsu.suntiq.api.response.vehicle.StatusUpdateResponse
import id.jsu.suntiq.api.response.vehicle.VehicleResponse
import id.jsu.suntiq.api.response.vehicle.cancel.CancelResponse
import id.jsu.suntiq.api.response.vehicle.confirmation.ConfirmationVehicleResponse
import id.jsu.suntiq.api.response.vehicle.delivery.ConfirmationDeliveryResponse
import id.jsu.suntiq.api.response.vehicle.finish.FinishConfirmationResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiFactory {

    /*GET*/

    @GET("vehicle/sync")
    fun getVehicle(@Query("pageSize") size: Int, @Query("page") page: Int): Observable<VehicleResponse>

    @GET("vehicle/sync/status")
    fun getStatusUpdate(): Observable<StatusUpdateResponse>

    /*POST*/
    @POST("login")
    fun login(@Body request: LoginRequest): Observable<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Observable<RegisterResponse>

    @POST("verification")
    fun verify(@Body requestBody: RequestBody): Observable<VerifyResponse>

    @POST("vehicle/show")
    fun detailVehicle(@Body request: DetailRequest): Observable<DetailVehicleResponse>

    @POST("delivery/office")
    fun listLocation(@Body request: LocationsRequest): Observable<LocationResponse>

    @POST("delivery/assignment")
    fun confirmVehicle(@Body requestBody: RequestBody): Observable<ConfirmationVehicleResponse>

    @POST("delivery/reception")
    fun confirmDeliveryOffice(@Body requestBody: RequestBody): Observable<ConfirmationDeliveryResponse>

    @POST("delivery/reception/verification")
    fun confirmFinishAssignment(@Body requestBody: RequestBody): Observable<FinishConfirmationResponse>

    @POST("delivery/assignment/cancel")
    fun cancelAssignment(@Body request: CancelRequest): Observable<CancelResponse>

    @PUT("vehicle/sync/status")
    fun completeUpdateData(@Body request: CompleteRequest): Observable<CompleteResponse>
}

