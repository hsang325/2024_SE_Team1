package com.example.skku_restaurant

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.*
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.route.RouteLine
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.*
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.TrackingManager
import com.getkeepsafe.relinker.ReLinker
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.example.skku_restaurant.viewmodel.RestaurantViewModel





class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var labelLayer: LabelLayer
    private lateinit var labelManager: LabelManager
    private var kakaoMap: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Kakao Map 네이티브 라이브러리 로드
        ReLinker.loadLibrary(this, "K3fAndroid")


        // Kakao Map SDK 초기화
        KakaoMapSdk.init(this, "2d1389b319739738a035f9914591c996")

        // 1. MapView 초기화
        mapView = findViewById(R.id.map_view)

        // 2. MapLifeCycleCallback 객체 생성
        val lifecycleCallback = object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출
                Log.d("KakaoMap", "Map destroyed")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                Log.e("KakaoMap", "Error occurred: ${error.message}", error)
            }
        }


        // 3. KakaoMapReadyCallback 객체 생성
        val readyCallback = object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                // 지도 준비 완료
                kakaoMap = map
                Log.d("KakaoMap", "onMapReady: KakaoMap is ready")

                // 기본 지도 설정
                setupMap()

                // 클릭 이벤트 설정
                setupMapClickListener()
                fetchNearbyRestaurants()
            }
        }


        // 3. MapView 시작
        mapView.start(lifecycleCallback)






    }



    private fun setupMap() {
        kakaoMap?.let { map ->
            // 서울시청 위치로 이동
            val seoulLocation = CameraUpdateFactory.newCenterPosition(LatLng.from(37.5665, 126.9780))
            map.moveCamera(seoulLocation)

            // 기본 라벨 추가
            addLabel(37.5665, 126.9780, "서울시청")
        }
    }
    private fun setupMapClickListener() {
        kakaoMap?.setOnMapClickListener { _, latLng, _, _ ->
            Log.d("KakaoMap", "Clicked location: ${latLng.latitude}, ${latLng.longitude}")
            addLabel(latLng.latitude, latLng.longitude, "클릭한 위치")
        }
    }

    // 라벨 추가 함수
    private fun addLabel(latitude: Double, longitude: Double, text: String) {
        // 1. LabelStyle 생성
        val labelStyle = LabelStyle.from()

        // 2. LabelOptions 생성
        val labelOptions = LabelOptions.from(LatLng.from(latitude, longitude))
            .setStyles(labelStyle)


        // 3. LabelLayer에 라벨 추가
        val label =labelLayer.addLabel(labelOptions)
        label.show()

    }
    // 맛집 검색 API 호출 및 지도에 마커 추가
    private fun fetchNearbyRestaurants() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(KakaoLocalSearchService::class.java)
        val call = service.searchRestaurants(
            "맛집",
            126.9780, // 예시: 서울 경도
            37.5665,  // 예시: 서울 위도
            2000 // 검색 반경 (단위: 미터)
        )

        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    response.body()?.documents?.forEach { restaurant ->
                        Log.d("KakaoLocalSearch", "Place: ${restaurant.placeName}, Lat: ${restaurant.latitude}, Lon: ${restaurant.longitude}")
                        // 지도에 마커 추가
                        addLabel(restaurant.latitude, restaurant.longitude, restaurant.placeName)
                    }
                } else {
                    Log.e("KakaoLocalSearch", "Response error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                Log.e("KakaoLocalSearch", "Failed to fetch data: ${t.message}")
            }
        })
    }
}


// Retrofit 서비스 인터페이스
interface KakaoLocalSearchService {
    @GET("v2/local/search/keyword.json")
    fun searchRestaurants(
        @Query("query") query: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double,
        @Query("radius") radius: Int
    ): Call<SearchResult>
}

// 검색 결과 데이터 클래스
data class SearchResult(
    val documents: List<Document>
)

data class Document(
    @SerializedName("place_name") val placeName: String,
    @SerializedName("x") val longitude: Double,
    @SerializedName("y") val latitude: Double
)