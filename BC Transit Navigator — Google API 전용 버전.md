<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

# BC Transit Navigator — Google API 전용 버전

> **BC Transit Navigator**는 구글 API만을 활용하여 개발된 캐나다 브리티시컬럼비아 지역의 대중교통 내비게이션 안드로이드 애플리케이션입니다. Java로 구축되었으며, **Material Design 3**와 **연두색 테마**를 기반으로 직관적이고 아름다운 사용자 경험을 제공합니다.

## 📋 목차

1. [프로젝트 개요](#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)
2. [구글 API 기반 핵심 기능](#%EA%B5%AC%EA%B8%80-api-%EA%B8%B0%EB%B0%98-%ED%95%B5%EC%8B%AC-%EA%B8%B0%EB%8A%A5)
3. [제외된 기능 및 제한사항](#%EC%A0%9C%EC%99%B8%EB%90%9C-%EA%B8%B0%EB%8A%A5-%EB%B0%8F-%EC%A0%9C%ED%95%9C%EC%82%AC%ED%95%AD)
4. [기술 스택 및 아키텍처](#%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D-%EB%B0%8F-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98)
5. [Material Design 3 연두색 테마](#material-design-3-%EC%97%B0%EB%91%90%EC%83%89-%ED%85%8C%EB%A7%88)
6. [화면별 상세 설명](#%ED%99%94%EB%A9%B4%EB%B3%84-%EC%83%81%EC%84%B8-%EC%84%A4%EB%AA%85)
7. [개발 환경 설정](#%EA%B0%9C%EB%B0%9C-%ED%99%98%EA%B2%BD-%EC%84%A4%EC%A0%95)
8. [API 구성 및 설정](#api-%EA%B5%AC%EC%84%B1-%EB%B0%8F-%EC%84%A4%EC%A0%95)

## 🎯 프로젝트 개요

### 구글 API 전용 설계 철학

BC Transit Navigator는 **구글 생태계의 API만을 활용**하여 구현되었습니다. 이를 통해 안정적인 서비스 제공, 일관된 개발 경험, 그리고 Google Play Services와의 완벽한 통합을 실현했습니다[^1][^2][^3].

### Material Design 3 Expressive 적용

Google의 최신 디자인 시스템인 **Material Design 3 Expressive**를 전면 도입하여 개인화된 사용자 경험을 제공합니다. **연두색(Light Green) 기반 테마**로 자연친화적이고 편안한 시각적 경험을 구현했습니다[^4][^5].

## 🚀 구글 API 기반 핵심 기능

| 기능 카테고리 | 구현 API | 상세 기능 |
| :-- | :-- | :-- |
| **지도 및 시각화** | Google Maps SDK for Android | 3D 지도, 위성/지형/하이브리드 뷰, 벡터 타일, 커스텀 마커, 폴리라인 경로 표시 |
| **경로 계획** | Routes API (2025년 표준) | 대중교통 경로 탐색, 다중 교통수단 조합, 실시간 교통 상황 반영 |
| **장소 검색** | Places API (New) | 자동완성 검색, 200만+ 비즈니스 정보, 접근성 정보, 전기차 충전소 정보 |
| **위치 서비스** | Fused Location Provider API | 고정밀 GPS/Wi-Fi 융합 위치, 배터리 최적화, 백그라운드 위치 추적 |
| **UI/UX** | Material Design 3 | 동적 색상 테마, 반응형 컴포넌트, 향상된 타이포그래피, 접근성 지원 |

### 구현 가능한 주요 기능들

**📍 정확한 경로 안내**

- Routes API를 통한 대중교통 경로 계산[^6]
- 도보, 버스, 지하철, 페리 등 다중 교통수단 조합
- 실시간 교통 상황을 반영한 소요시간 예측
- 환승 지점 및 도보 구간 상세 안내

**🗺️ 고급 지도 기능**

- Google Maps SDK의 모든 기능 활용[^3]
- 3D 지도, 실내 지도, 위성 이미지 지원
- 커스텀 마커와 정보 창
- 경로 오버레이 및 실시간 애니메이션

**🔍 스마트 검색**

- 새로운 Places API의 향상된 검색 기능[^7]
- 접근성 정보 (휠체어 접근 가능성)
- 실시간 전기차 충전소 정보
- 200개 이상의 장소 유형 지원

**📱 Material Design 3 경험**

- 연두색 기반 동적 색상 테마
- 유동적이고 자연스러운 애니메이션[^4]
- 개인화된 Quick Settings
- Live Updates 알림 시스템


## ❌ 제외된 기능 및 제한사항

### 실시간 대중교통 정보 제한

구글 API만으로는 다음 기능들이 **구현 불가능**하거나 **제한적**입니다:


| 제외된 기능 | 제한 이유 | 대안 |
| :-- | :-- | :-- |
| **실시간 버스 위치 추적** | TransLink/BC Transit 전용 API 필요 | 정적 경로 정보 및 예상 도착시간 제공 |
| **정류장별 실시간 도착 시간** | GTFS-realtime 피드 직접 연동 필요[^8] | Routes API 기반 예상 시간 |
| **운행 지연/중단 알림** | 지역 교통청 실시간 피드 필요 | 일반적인 교통 상황 정보 |
| **정확한 환승 타이밍 알림** | 실시간 차량 위치 데이터 필요 | GPS 기반 근사 알림 |

### 구현 방식 변경사항

- **실시간 추적** → **예측 기반 안내**: Routes API의 실시간 교통 정보 활용
- **정밀 알림** → **근사 알림**: Fused Location Provider 기반 위치 알림
- **실시간 지연 정보** → **일반 교통 상황**: Google Maps 교통 레이어 정보


## 🛠 기술 스택 및 아키텍처

### 순수 구글 생태계 기술 스택

| 레이어 | 구글 API/라이브러리 | 용도 |
| :-- | :-- | :-- |
| **UI** | Material Design 3, AndroidX | 연두색 테마 기반 UI 구성 |
| **지도** | Google Maps SDK for Android | 지도 렌더링 및 상호작용 |
| **경로** | Routes API (2025년 표준) | 대중교통 경로 계산 |
| **검색** | Places API (New) | 장소 검색 및 자동완성 |
| **위치** | Fused Location Provider API | 고정밀 위치 서비스 |
| **데이터** | Room (SQLite) | 로컬 데이터 저장 |
| **네트워크** | Retrofit 2 + OkHttp | Google API 통신 |

### 아키텍처 패턴

```
+-------------+        +----------------+        +-------------------+
|  View (UI)  | <----> |   ViewModel    | <----> |    Repository     |
| Material 3  |        | LiveData       |        | (Google APIs)     |
+-------------+        +----------------+        +-------------------+
       ↑                         ↑                         ↑
       |                   FusedLocationClient        Routes API
       |                         ↑                         ↑
       |                         |                         |
+-------------+        +--------------------+       +------------------+
| Google Maps |        |   Places API      |       |   Room Database   |
|    SDK      |        |   (New)           |       |   (Local Cache)   |
+-------------+        +--------------------+       +------------------+
```


## 🎨 Material Design 3 연두색 테마

### 연두색 컬러 팔레트

Google Material Design의 공식 연두색 팔레트를 기반으로 구성[^9][^10]:


| 컬러 역할 | 컬러 코드 | 사용 용도 |
| :-- | :-- | :-- |
| **Primary** | `#8bc34a` (Light Green) | 주요 액션 버튼, 앱바 |
| **Primary Light** | `#c5e1a5` (Light Green Lighten-3) | 선택된 상태, 호버 효과 |
| **Primary Dark** | `#689f38` (Light Green Darken-2) | 강조 요소, 그림자 |
| **Accent** | `#b2ff59` (Light Green Accent-2) | 플로팅 액션 버튼, 중요 알림 |
| **Background** | `#f1f8e9` (Light Green Lighten-5) | 앱 배경, 카드 배경 |
| **Surface** | `#dcedc8` (Light Green Lighten-4) | 카드, 다이얼로그 |

### Material 3 Expressive 특징

- **유동적 애니메이션**: 자연스러운 스프링 애니메이션 적용[^4]
- **동적 색상**: 사용자 설정에 따른 색상 조정
- **반응형 컴포넌트**: 터치 피드백과 상태 변화
- **개선된 접근성**: 색상 대비 및 터치 영역 최적화


## 📱 화면별 상세 설명

### 1. 메인 홈 화면 (MainActivity)

#### 1.1 Material 3 상단 앱바

**구성 요소:**

- **연두색 그라데이션 배경**: Primary에서 Primary Light로 자연스러운 그라데이션
- **Material 3 내비게이션**: 햄버거 메뉴 → Navigation Rail (태블릿) 자동 적응
- **동적 타이틀**: "BC Transit Navigator" with Material 3 Typography
- **액션 칩 버튼**: 검색, 설정을 Material 3 Chip 컴포넌트로 구현


#### 1.2 스마트 카드 섹션

**Material 3 카드 디자인:**

- **현재 위치 카드**:
    - Fused Location Provider의 정확도 정보를 Material 3 Progress Indicator로 표시
    - 주소 변환은 Places API Geocoding 활용
    - 연두색 테마의 위치 아이콘과 새로고침 FAB
- **즐겨찾기 카드 그리드**:
    - Material 3 Card with 연두색 accent border
    - 각 카드는 elevation과 tonal color overlay 적용
    - 길게 누르기 시 Material 3 menu 표시


#### 1.3 대중교통 정보 패널

**제한된 실시간 정보:**

- **근처 정류장**: Places API의 "transit_station" 유형으로 검색
- **예상 도착 시간**: Routes API의 정적 스케줄 정보 활용
- **교통 상황**: Google Maps Traffic Layer 정보 반영
- **Material 3 List**: 확장 가능한 아코디언 구조


### 2. 통합 지도 화면 (MapsActivity)

#### 2.1 Google Maps 컨테이너

**Maps SDK 고급 기능 활용:**

- **3D 지도**: tilt와 rotation 지원으로 입체적 경로 확인
- **동적 마커**: 연두색 테마 커스텀 마커 디자인
- **실시간 교통 레이어**: Traffic Layer로 도로 상황 표시
- **Transit Layer**: 대중교통 노선 오버레이


#### 2.2 Material 3 컨트롤 패널

**FAB와 Mini FAB 활용:**

- **줌 컨트롤**: Material 3 Extended FAB
- **현재 위치**: 연두색 Primary FAB with 맥동 효과
- **레이어 선택**: Material 3 Segmented Button
- **나침반**: 필요시 자동 표시되는 Mini FAB


#### 2.3 하단 시트 (Bottom Sheet)

**Material 3 Bottom Sheet:**

- **다단계 확장**: Peek → Half → Full expansion
- **정류장 정보**: Places API Details로 상세 정보 제공
- **경로 옵션**: Routes API의 다양한 경로 대안 표시
- **Material 3 Chip**: 경로 필터링 옵션


### 3. 경로 탐색 화면

#### 3.1 검색 인터페이스

**Places API New 활용:**

- **자동완성 검색**: 200개+ 장소 유형 지원[^7]
- **접근성 정보**: 휠체어 접근성 등 상세 정보
- **Material 3 Search Bar**: 연두색 focus state
- **음성 검색**: Android Speech Recognition API 연동


#### 3.2 Routes API 경로 결과

**2025년 표준 Routes API 활용[^11][^6]:**

- **향상된 성능**: 기존 Directions API 대비 빠른 응답
- **실시간 교통**: 각 경로 구간별 실시간 교통 상황
- **요금 정보**: 대중교통 요금 데이터 제공
- **다양한 교통수단**: 도보, 자전거, 대중교통 조합


#### 3.3 Material 3 경로 카드

- **확장 가능한 카드**: Material 3 Expansion Panel
- **진행 표시**: Linear Progress Indicator with 연두색
- **비교 차트**: Material 3 의 데이터 시각화 컴포넌트
- **액션 버튼**: Primary 버튼 with 연두색 테마


### 4. 내비게이션 화면

#### 4.1 실시간 안내 (제한적)

**Fused Location Provider 기반:**

- **GPS 추적**: 고정밀 위치 추적으로 경로 진행 상황 파악
- **근사 알림**: 정류장 100m 이내 접근 시 Material 3 Snackbar 알림
- **진동 피드백**: Haptic Feedback API로 상황별 진동 패턴


#### 4.2 Material 3 진행 표시

- **상단 Progress Rail**: 전체 여행 진행도를 시각적으로 표현
- **단계별 표시**: 각 교통수단별 Material 3 아이콘
- **연두색 강조**: 현재 단계를 Primary 색상으로 하이라이트


### 5. 설정 및 히스토리 화면

#### 5.1 Material 3 설정 화면

**동적 색상 테마 설정:**

- **테마 선택**: 라이트/다크/시스템 자동
- **연두색 강도**: Primary 색상의 saturation 조절
- **Material You**: 사용자 wallpaper 기반 동적 색상 생성[^12]


#### 5.2 Room 기반 히스토리

- **로컬 저장**: 개인정보 보호를 위한 기기 내 저장
- **Material 3 Charts**: 이용 통계를 아름다운 차트로 표시
- **검색 기능**: 히스토리 내 빠른 검색


## ⚙️ 개발 환경 설정

### 필수 요구사항

- Android Studio Hedgehog 2025.x+
- JDK 17 (Android Studio 포함)
- Google Cloud 프로젝트 (Maps, Places, Routes API 활성화)
- Material Design 3 라이브러리


### 빌드 구성

```bash
git clone https://github.com/your-org/bc-transit-navigator-google.git
cd bc-transit-navigator-google
./gradlew assembleDebug
```


### 의존성 설정

```gradle
dependencies {
    // Material Design 3
    implementation "androidx.compose.material3:material3:$material3_version"
    
    // Google Maps & Location
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    implementation 'com.google.android.gms:play-services-location:21.0.1'
    
    // Google APIs
    implementation 'com.google.maps.android:maps-ktx:3.4.0'
    implementation 'com.google.android.libraries.places:places:3.2.0'
    
    // Room Database
    implementation 'androidx.room:room-runtime:2.5.0'
    
    // Network
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
}
```


## 🔑 API 구성 및 설정

### Google Cloud Console 설정

1. **Maps SDK for Android** 활성화
2. **Routes API** 활성화 (2025년 표준)[^11]
3. **Places API (New)** 활성화[^7]
4. **Geocoding API** 활성화

### API 키 설정

`app/src/main/res/values/secrets.xml`:

```xml
<resources>
    <string name="google_maps_key">YOUR_GOOGLE_MAPS_API_KEY</string>
    <string name="google_routes_key">YOUR_ROUTES_API_KEY</string>
    <string name="google_places_key">YOUR_PLACES_API_KEY</string>
</resources>
```


### 권한 설정

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.VIBRATE" />
```


## 🎯 결론

BC Transit Navigator Google API 전용 버전은 **구글 생태계의 강력한 API들을 최대한 활용**하여 안정적이고 일관된 대중교통 내비게이션 경험을 제공합니다.

비록 실시간 버스 추적과 같은 일부 고급 기능은 제외되었지만, **Routes API의 실시간 교통 정보**, **Places API의 풍부한 장소 데이터**, **Fused Location Provider의 정확한 위치 서비스**를 통해 실용적인 대중교통 앱을 구현할 수 있습니다.

**Material Design 3의 연두색 테마**는 BC 지역의 자연환경과 조화를 이루며, 사용자에게 편안하고 직관적인 경험을 제공합니다. 구글의 최신 디자인 시스템을 활용하여 미래지향적이고 접근성이 뛰어난 앱을 만들 수 있습니다.

---
> 🌲 Google APIs와 Material Design 3으로 만든 **친환경적이고 아름다운** 브리티시컬럼비아 대중교통 내비게이터

<div style="text-align: center">⁂</div>

[^1]: https://developers.google.com/maps/documentation/directions/overview

[^2]: https://localyse.eu/the-new-places-api-the-place-to-be-for-smarter-location-data/

[^3]: https://developers.google.com/maps/documentation/android-sdk

[^4]: https://blog.google/products/android/material-3-expressive-android-wearos-launch/

[^5]: https://developer.android.com/develop/ui/compose/designsystems/material3

[^6]: https://mapsplatform.google.com/resources/blog/routes-api-now-generally-available/

[^7]: https://mapsplatform.google.com/resources/blog/introducing-the-new-places-api-with-access-to-new-ev-accessibility-features-and-more/

[^8]: https://developers.google.com/transit/gtfs-realtime

[^9]: https://colorswall.com/palette/313060

[^10]: https://colorswall.com/palette/313061

[^11]: https://masterconcept.ai/news/google-maps-api-changes-2025-migration-guide-for-directions-api-distance-matrix-api/

[^12]: https://pypi.org/project/materialyoucolor/1.2.0/

[^13]: https://developer.android.com/develop/sensors-and-location/location/maps-and-places

[^14]: https://mapsplatform.google.com/resources/blog/announcing-routes-api-new-enhanced-version-directions-and-distance-matrix-apis/

[^15]: https://google.globema.com/2024/07/04/deliver-more-valuable-information-with-the-new-google-places-api/

[^16]: https://groups.google.com/g/gtfs-realtime/c/_r7RDTkz3Pw

[^17]: https://material-theme.com/docs/reference/color-palette/

[^18]: https://support.google.com/transitpartners/answer/2529130

[^19]: https://info.keylimeinteractive.com/creating-a-custom-material-ui-theme

[^20]: https://m3.material.io

[^21]: https://developer.android.com/develop/sensors-and-location/location/retrieve-current

[^22]: https://docs.mapsindoors.com/sdks-and-frameworks/android/user-positioning/using-google-fused-location-provider

[^23]: https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi

[^24]: https://colorswall.com/palette/105557

[^25]: https://github.com/googlemaps/android-maps-utils

[^26]: https://developers.google.com/maps/documentation/routes/transit-route

[^27]: https://tech.news.am/eng/news/5399/google-prepares-a-vibrant-android-redesign-what-to-expect-from-material-design-3-expressive.html

[^28]: https://developer.android.com/develop/sensors-and-location/location

