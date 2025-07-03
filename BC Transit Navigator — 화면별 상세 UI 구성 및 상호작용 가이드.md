<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

# BC Transit Navigator — 화면별 상세 UI 구성 및 상호작용 가이드

BC Transit Navigator의 Google API 전용 버전에서 사용되는 **Material Design 3 연두색 테마** 기반의 화면별 위젯, UI 구성, 상호작용 및 애니메이션을 상세히 설명합니다.

## 🏠 1. 메인 홈 화면 (MainActivity)

### 1.1 상단 앱바 (TopAppBar) 구성

**Material 3 Large TopAppBar 적용**

- **컨테이너**: `surfaceContainerHighest` 배경에 연두색 `#8bc34a` 그라데이션 오버레이[^1]
- **타이틀**: Material 3 `headlineMedium` 타이포그래피, "BC Transit Navigator" 텍스트
- **내비게이션 아이콘**: 햄버거 메뉴 (`Icons.Default.Menu`)
- **액션 아이콘들**: 검색 (`Icons.Default.Search`), 설정 (`Icons.Default.Settings`)

**상호작용 및 애니메이션**

- **햄버거 메뉴 탭**: 0.3초 **spring animation**으로 내비게이션 드로어 슬라이드 인[^2]
- **액션 버튼 리플 효과**: Material 3 `RippleEffect`로 연두색 계열 리플 확산
- **스크롤 시 축소**: 스크롤 다운 시 Large에서 Center TopAppBar로 축소 애니메이션


### 1.2 빠른 접근 카드 섹션

**현재 위치 카드 (Material 3 Card)**

```kotlin
// Material 3 Card with 연두색 테마
Card(
    modifier = Modifier.fillMaxWidth().padding(16.dp),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
)
```

**위젯 구성**

- **위치 정확도 인디케이터**: `CircularProgressIndicator`로 GPS 정확도 표시
- **주소 텍스트**: Fused Location Provider API로 얻은 좌표를 Places API Geocoding으로 변환[^3]
- **새로고침 FAB**: 연두색 `Mini FloatingActionButton`[^4]

**상호작용**

- **카드 탭**: 0.2초 **scale animation** (0.95 → 1.0) 후 지도 화면으로 전환
- **새로고침 버튼**: 탭 시 **rotation animation** 360도 회전하며 위치 갱신
- **길게 누르기**: **haptic feedback**과 함께 위치 공유 메뉴 표시

**즐겨찾기 카드 그리드**

- **LazyVerticalGrid**: 2열 그리드 레이아웃
- **각 카드**: Material 3 `OutlinedCard`로 연두색 `#c5e1a5` 테두리
- **아이콘**: Material Icons (`Icons.Default.Home`, `Icons.Default.Work` 등)


### 1.3 실시간 대중교통 정보 패널

**근처 정류장 리스트 (Material 3 List)**

```kotlin
LazyColumn {
    items(nearbyStations) { station ->
        ListItem(
            headlineContent = { Text(station.name) },
            supportingContent = { Text("${station.distance}m away") },
            leadingContent = { Icon(Icons.Default.DirectionsBus) },
            trailingContent = { Text("${station.nextArrival} min") }
        )
    }
}
```

**상호작용 및 애니메이션**

- **확장 가능한 아코디언**: 탭 시 **expandVertically animation**으로 상세 정보 표시
- **실시간 카운트다운**: `AnimatedContent`로 도착 시간 숫자 변경 애니메이션
- **상태 변경 애니메이션**: 정상/지연/중단 상태에 따른 색상 **cross-fade** 전환


### 1.4 하단 네비게이션 바

**Material 3 NavigationBar**

```kotlin
NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = Color(0xFF8bc34a) // 연두색 테마
) {
    // 5개 탭: 홈, 지도, 경로, 히스토리, 프로필
}
```

**상호작용**

- **탭 전환**: **slide transition**으로 화면 전환
- **배지 애니메이션**: 새 알림 시 **bounce animation**으로 숫자 배지 표시
- **선택 상태**: 활성 탭의 아이콘 **scale up** 및 색상 변경


## 🗺️ 2. 통합 지도 화면 (MapsActivity)

### 2.1 Google Maps 컨테이너

**Maps SDK for Android 설정**

```kotlin
GoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
googleMap.isTrafficEnabled = true
googleMap.isTransitLayerEnabled = true
```

**커스텀 마커 및 오버레이**

- **현재 위치 마커**: 연두색 `#8bc34a` 색상의 **pulsing animation** 마커[^5]
- **정류장 마커**: 노선별 동적 색상 마커, 탭 시 **bounce animation**
- **경로 폴리라인**: Routes API 데이터로 다중 색상 경로 표시[^6]

**지도 제스처 및 애니메이션**

- **핀치 줌**: 매끄러운 **zoom transition**
- **카메라 이동**: `animateCamera()`로 **easing animation** 적용
- **마커 클러스터링**: 줌 레벨에 따른 **cluster expand/collapse** 애니메이션


### 2.2 지도 컨트롤 패널

**플로팅 액션 버튼 그룹**

```kotlin
// 현재 위치 FAB
FloatingActionButton(
    onClick = { /* 현재 위치로 이동 */ },
    containerColor = Color(0xFF8bc34a),
    contentColor = Color.White
) {
    Icon(Icons.Default.MyLocation)
}
```

**위젯 구성**[^4]

- **현재 위치 FAB**: 연두색 Primary FAB, **pulse effect** 적용
- **줌 컨트롤**: **Extended FAB** 스타일의 +/- 버튼
- **레이어 선택**: Material 3 `SegmentedButton`으로 교통/위성/지형 선택
- **나침반**: 지도 회전 시 자동 표시되는 **fade in/out** 애니메이션

**상호작용**

- **FAB 탭**: **scale animation** (1.0 → 1.1 → 1.0) 후 기능 실행
- **길게 누르기**: **haptic feedback**과 함께 빠른 액션 메뉴 표시
- **레이어 변경**: **cross-fade transition**으로 지도 레이어 전환


### 2.3 하단 시트 (Bottom Sheet)

**Material 3 ModalBottomSheet**[^7]

```kotlin
ModalBottomSheet(
    onDismissRequest = { /* 닫기 */ },
    sheetState = bottomSheetState,
    dragHandle = { BottomSheetDefaults.DragHandle() }
) {
    // 정류장 또는 경로 정보 표시
}
```

**다단계 확장 상태**

- **Peek State**: 64dp 높이로 기본 정보만 표시
- **Half Expanded**: 화면 50% 높이로 중간 정보 표시
- **Fully Expanded**: 전체 화면으로 상세 정보 표시

**내부 컨텐츠 구성**

- **드래그 핸들**: Material 3 표준 드래그 인디케이터
- **정류장 정보**: Places API Details로 상세 정보 표시[^8]
- **경로 옵션**: Routes API 결과를 Material 3 Card로 구성[^9]

**상호작용 및 애니메이션**

- **드래그 제스처**: **spring animation**으로 부드러운 확장/축소
- **스냅 동작**: 중간 지점에서 자동으로 가장 가까운 상태로 **snap**
- **내용 전환**: 확장 상태에 따른 **animated visibility** 변경


## 🔍 3. 검색 및 경로 계획 화면

### 3.1 Material 3 SearchBar

**SearchBar 위젯 구성**[^10]

```kotlin
SearchBar(
    inputField = {
        SearchBarDefaults.InputField(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = { Text("목적지 검색...") },
            leadingIcon = { Icon(Icons.Default.Search) }
        )
    },
    expanded = isExpanded,
    onExpandedChange = { isExpanded = it }
)
```

**Places API 자동완성 통합**[^3]

- **실시간 검색**: 사용자 입력에 따른 즉시 Places Autocomplete 호출
- **예측 결과**: `LazyColumn`으로 자동완성 결과 표시
- **결과 하이라이트**: 검색어와 일치하는 부분 **bold** 처리

**상호작용 및 애니메이션**

- **포커스 애니메이션**: 검색바 탭 시 **expand animation**으로 전체 화면 전환
- **타이핑 애니메이션**: 자동완성 결과 **staggered animation**으로 순차 표시
- **선택 애니메이션**: 결과 선택 시 **slide up** 후 다음 화면 전환


### 3.2 경로 필터링 칩

**Material 3 Chip Group**[^11]

```kotlin
LazyRow {
    items(filterOptions) { filter ->
        FilterChip(
            selected = filter.isSelected,
            onClick = { toggleFilter(filter) },
            label = { Text(filter.name) },
            leadingIcon = if (filter.isSelected) {
                { Icon(Icons.Default.Check) }
            } else null
        )
    }
}
```

**칩 유형별 구성**[^12]

- **Filter Chip**: 최단시간, 최소환승, 접근성 우선 필터
- **Assist Chip**: 음성검색, QR스캔 등 보조 기능
- **Input Chip**: 선택된 출발지/목적지 표시

**상호작용**

- **선택 애니메이션**: **cross-fade** 색상 변경 및 체크 아이콘 **scale in**
- **그룹 선택**: 다중 선택 시 **wave animation**으로 순차 선택 표시


### 3.3 Routes API 경로 결과

**경로 카드 구성**

```kotlin
Card(
    modifier = Modifier.animateContentSize(), // 확장 애니메이션
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
    Column {
        // 경로 요약 정보
        // 확장 시 상세 단계별 안내
    }
}
```

**실시간 정보 표시**[^6]

- **교통 상황**: Routes API의 실시간 traffic 정보 반영
- **요금 정보**: 대중교통 요금 데이터 표시
- **소요시간**: 실시간 업데이트되는 ETA

**상호작용 및 애니메이션**

- **카드 확장**: `animateContentSize()`로 부드러운 높이 변경
- **경로 비교**: 여러 경로 간 **slide transition**으로 전환
- **선택 피드백**: 경로 선택 시 **emphasis animation** 적용


## 🧭 4. 내비게이션 화면

### 4.1 실시간 진행 표시

**상단 Progress Rail**

```kotlin
LinearProgressIndicator(
    progress = currentProgress,
    modifier = Modifier.fillMaxWidth(),
    color = Color(0xFF8bc34a), // 연두색 테마
    trackColor = Color(0xFFc5e1a5) // 연두색 Light
)
```

**단계별 진행 표시**

- **교통수단 아이콘**: 도보, 버스, 지하철 등 Material Icons
- **현재 단계 강조**: **scale animation**과 연두색 하이라이트
- **완료된 단계**: **check mark animation**으로 완료 표시


### 4.2 지시사항 카드

**Material 3 Card with Animation**

```kotlin
Card(
    modifier = Modifier.animateContentSize(),
    colors = CardDefaults.cardColors(
        containerColor = Color(0xFF8bc34a).copy(alpha = 0.1f)
    )
) {
    // 현재 지시사항 및 다음 단계 정보
}
```

**동적 업데이트**

- **거리 기반 갱신**: Fused Location Provider로 실시간 위치 추적
- **지시사항 변경**: **slide transition**으로 새 지시사항 표시
- **음성 안내**: Text-to-Speech API 연동


### 4.3 알림 시스템

**Material 3 Snackbar**

```kotlin
Snackbar(
    action = {
        TextButton(onClick = { /* 확인 */ }) {
            Text("확인", color = Color(0xFF8bc34a))
        }
    },
    containerColor = MaterialTheme.colorScheme.inverseSurface
) {
    Text("정류장 100m 앞입니다")
}
```

**알림 유형별 애니메이션**

- **승차 알림**: **bounce animation**과 진동 패턴
- **환승 알림**: **slide from top** 애니메이션
- **하차 알림**: **emphasis animation**과 강한 진동


## ⚙️ 5. 설정 및 히스토리 화면

### 5.1 Material 3 설정 화면

**PreferenceScreen 구성**

```kotlin
LazyColumn {
    item {
        PreferenceCategory(title = "테마 설정")
        SwitchPreference(
            title = "다크 모드",
            checked = isDarkMode,
            onCheckedChange = { isDarkMode = it }
        )
    }
}
```

**연두색 테마 커스터마이징**

- **컬러 피커**: 연두색 계열 색상 선택기
- **Dynamic Color**: Material You 기반 동적 색상 생성[^13]
- **미리보기**: 실시간 테마 변경 미리보기


### 5.2 히스토리 통계 차트

**Material 3 Charts (Compose)**[^14]

```kotlin
// 이용 통계 차트
LineChart(
    data = usageData,
    modifier = Modifier.height(200.dp),
    lineColor = Color(0xFF8bc34a)
)
```

**데이터 시각화**

- **이용 빈도**: 연두색 막대 그래프
- **절약 효과**: 원형 차트로 CO₂ 절약량 표시
- **월간 통계**: 라인 차트로 월별 이용 추이

**상호작용**

- **차트 탭**: **zoom in animation**으로 상세 정보 표시
- **데이터 포인트**: **ripple effect**로 수치 표시
- **필터 변경**: **cross-fade transition**으로 차트 데이터 변경


## 🎨 공통 애니메이션 및 전환 효과

### Material 3 Motion System

**표준 애니메이션 Duration**[^2]

- **빠른 전환**: 100-200ms (버튼 리플, 상태 변경)
- **표준 전환**: 300ms (화면 전환, 카드 애니메이션)
- **복잡한 전환**: 500ms (레이아웃 변경, 내비게이션)

**Easing 함수**

- **Standard**: 일반적인 UI 전환
- **Decelerate**: 진입 애니메이션
- **Accelerate**: 종료 애니메이션
- **Spring**: 물리적 느낌의 상호작용


### 연두색 테마 일관성

**색상 일관성 유지**

- 모든 **Primary Action**: `#8bc34a` (Light Green)
- **Selection State**: `#c5e1a5` (Light Green Lighten-3)
- **Background Tint**: `#f1f8e9` (Light Green Lighten-5)

이러한 상세한 UI 구성과 상호작용 설계를 통해 BC Transit Navigator는 **Google API의 강력한 기능**과 **Material Design 3의 아름다운 디자인**을 결합하여 직관적이고 효율적인 대중교통 내비게이션 경험을 제공합니다.

<div style="text-align: center">⁂</div>

[^1]: https://www.mdui.org/en/

[^2]: https://uxplanet.org/10-principles-of-animation-in-material-design-53b870e74629

[^3]: https://developers.google.com/maps/documentation/places/web-service/place-autocomplete

[^4]: https://composables.com/material3/floatingactionbutton

[^5]: https://developers.google.com/maps/documentation/android-sdk/controls

[^6]: https://mapsplatform.google.com/resources/blog/routes-api-now-generally-available/

[^7]: https://m3.material.io/components/bottom-sheets/overview

[^8]: https://blog.afi.io/blog/google-address-autocomplete-with-the-places-api/

[^9]: https://mapsplatform.google.com/resources/blog/announcing-routes-api-new-enhanced-version-directions-and-distance-matrix-apis/

[^10]: https://www.geeksforgeeks.org/material-search-bar-in-android/

[^11]: https://www.boltuix.com/2025/06/ultimate-guide-to-material-3-chips-in.html

[^12]: https://composables.com/material3/inputchip

[^13]: https://9to5google.com/2022/03/15/material-you-widgets/

[^14]: https://www.youtube.com/watch?v=qhOZ145p9DI

[^15]: https://www.phonearena.com/news/google-maps-for-android-buttons-ui-elements-new-color-update-rolling-out_id165781

[^16]: https://design-encyclopedia.com/?E=460466\&I=M

[^17]: https://m3.material.io

[^18]: https://9to5google.com/2021/10/29/android-12-material-you-widgets/

[^19]: https://m3.material.io/components

[^20]: https://www.androidpolice.com/check-out-these-material-you-widgets-google-is-bringing-to-android-12/

[^21]: https://blog.truegeometry.com/tutorials/education/7864014b90f735caa043266fc9eb8700/JSON_TO_ARTCL_Shadows_and_Elevation_in_context_of_Material_Design.html

[^22]: https://literature.rockwellautomation.com/idc/groups/literature/documents/um/motion-um002_-en-p.pdf

[^23]: https://m3.material.io/components/bottom-sheets/guidelines

[^24]: https://www.youtube.com/watch?v=0CQvoYidesw

[^25]: https://literature.rockwellautomation.com/idc/groups/literature/documents/rm/motion-rm002_-en-p.pdf

[^26]: https://m3.material.io/components/navigation-bar/overview

[^27]: https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-floating-action-button.html

[^28]: https://dev.to/mar1anna/create-a-search-bar-with-react-and-material-ui-4he

[^29]: https://mapsplatform.google.com/resources/blog/next-generation-autocomplete-is-now-available-in-preview/

[^30]: https://cloud.google.com/blog/products/maps-platform/new-advanced-routing-and-map-customization-capabilities

