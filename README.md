# common-web

Spring Boot 프로젝트에서 공통으로 사용하는 유틸리티 라이브러리입니다.

- 표준 API 응답 래퍼 (`ApiResponse`)
- 번호 기반 페이징 응답 DTO (`PageResponse`)
- 커서 기반 페이징 응답 DTO (`CursorPageResponse`)
- 전역 예외 핸들러 (`GlobalExceptionHandler`)

---

## 의존성 추가

### 1. GitHub Packages 저장소 등록

`build.gradle`에 저장소와 의존성을 추가합니다.

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/psyoona/common-web")
        credentials {
            username = findProperty('githubActor') ?: System.getenv('GITHUB_ACTOR')
            password = findProperty('githubToken') ?: System.getenv('GITHUB_TOKEN')
        }
    }
}

dependencies {
    implementation 'com.yoonslab:common-web:1.01.000'
}
```

### 2. GitHub 인증 정보 설정

`~/.gradle/gradle.properties` (Windows: `C:\Users\사용자명\.gradle\gradle.properties`) 에 추가합니다.

```properties
githubActor=psyoona
githubToken=ghp_xxxxxxxxxxxxxxxxxxxx
```

> GitHub Personal Access Token은 **Settings → Developer settings → Personal access tokens (classic)** 에서 `read:packages` 권한으로 발급합니다.

---

## 사용법

### ApiResponse — 표준 API 응답 래퍼

모든 API 응답을 `{ success, message, data }` 구조로 통일합니다.

```java
// 성공 (데이터만)
ApiResponse<UserDto> res = ApiResponse.ok(userDto);

// 성공 (메시지 + 데이터)
ApiResponse<UserDto> res = ApiResponse.ok("조회 성공", userDto);

// 실패
ApiResponse<Void> res = ApiResponse.fail("존재하지 않는 리소스");
```

컨트롤러 적용 예시:

```java
@GetMapping("/{id}")
public ApiResponse<UserDto> getUser(@PathVariable Long id) {
    return ApiResponse.ok(userService.findById(id));
}
```

응답 JSON:
```json
{ "success": true, "message": null, "data": { "id": 1, "name": "홍길동" } }
{ "success": false, "message": "존재하지 않는 리소스", "data": null }
```

---

### PageResponse — 번호 기반 페이징

1~10 단위 페이지 블록 네비게이션을 지원합니다.

```java
// Service
Page<User> page = userRepository.findAll(PageRequest.of(pageNum - 1, 10));
PageResponse<UserDto> res = new PageResponse<>(
    page.getContent().stream().map(UserDto::from).toList(),
    pageNum,
    10,
    page.getTotalElements()
);

// Controller
@GetMapping
public ApiResponse<PageResponse<UserDto>> list(@RequestParam(defaultValue = "1") int page) {
    return ApiResponse.ok(userService.getList(page));
}
```

응답 JSON:
```json
{
  "success": true,
  "data": {
    "content": [...],
    "page": 1,
    "size": 10,
    "totalElements": 25,
    "totalPages": 3,
    "startPage": 1,
    "endPage": 3,
    "hasPrevious": false,
    "hasNext": false
  }
}
```

---

### CursorPageResponse — 커서 기반 페이징

대용량 데이터에서 offset 방식보다 효율적입니다. 마지막 항목의 ID를 커서로 사용합니다.

```java
// Service
List<Post> posts = postRepository.findByCursorId(cursor, PageRequest.of(0, size + 1));
boolean hasNext = posts.size() > size;
if (hasNext) posts = posts.subList(0, size);

CursorPageResponse<PostDto> res = CursorPageResponse.<PostDto>builder()
    .content(posts.stream().map(PostDto::from).toList())
    .nextCursor(hasNext ? posts.get(posts.size() - 1).getId() : null)
    .hasNext(hasNext)
    .size(size)
    .totalElements(null) // 대용량 시 count 쿼리 생략 가능
    .build();

// Controller
@GetMapping
public ApiResponse<CursorPageResponse<PostDto>> list(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "20") int size) {
    return ApiResponse.ok(postService.getList(cursor, size));
}
```

응답 JSON:
```json
{
  "success": true,
  "data": {
    "content": [...],
    "nextCursor": 42,
    "hasNext": true,
    "size": 20,
    "totalElements": null
  }
}
```

---

### GlobalExceptionHandler — 전역 예외 핸들러

`@SpringBootApplication`의 `scanBasePackages`에 `com.yoonslab.common`을 추가하면 자동으로 등록됩니다.

```java
@SpringBootApplication(scanBasePackages = {"com.example.myapp", "com.yoonslab.common"})
public class MyApplication { ... }
```

| 예외 | HTTP 상태 |
|---|---|
| `IllegalArgumentException` | 400 Bad Request |
| `NoSuchElementException` | 404 Not Found |

```java
// 서비스에서 그냥 던지면 핸들러가 ApiResponse로 감싸서 반환
throw new NoSuchElementException("사용자를 찾을 수 없습니다.");
throw new IllegalArgumentException("유효하지 않은 상태값입니다.");
```

---

## 개발 및 배포

### 로컬 실행 (콘솔 데모)
```bash
./gradlew runDemo
```

### 테스트
```bash
./gradlew test
```

### 로컬 Maven 저장소 배포
```bash
./gradlew publishToMavenLocal
```

### GitHub Packages 배포
```bash
./gradlew publishMavenJavaPublicationToGitHubPackagesRepository
```

> 배포 전 `gradle.properties`의 `version`을 올려야 합니다. GitHub Packages는 같은 버전 덮어쓰기를 허용하지 않습니다.

### master push 시 자동 배포 (GitHub Actions)

`.github/workflows/publish.yml`이 포함되어 있습니다. master 브랜치에 push하면 자동으로 GitHub Packages에 배포됩니다. 별도 설정 없이 `GITHUB_TOKEN`이 자동 주입됩니다.
