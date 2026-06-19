# CH20 Assignment Guide

## 1. 패키지 설치

```bash
pip install -r requirements.txt
```

## 2. 마이그레이션

```bash
python manage.py migrate
```

`rest_framework.authtoken`을 추가했기 때문에 위 명령을 다시 실행해야 토큰 테이블이 생성됩니다.

## 3. 토큰 생성

```bash
python manage.py drf_create_token <django_username>
```

출력된 토큰을 `app/src/main/java/com/cookandroid/project9_4/MainActivity.java`의
`AUTH_TOKEN` 값에 붙여 넣습니다.

## 4. 로컬 Django 실행

```bash
python manage.py runserver
```

확인할 주소:

- 메인 페이지: `http://127.0.0.1:8000/`
- API 루트: `http://127.0.0.1:8000/api_root/`
- 게시글 API: `http://127.0.0.1:8000/api_root/Post/`
- 토큰 발급 API: `http://127.0.0.1:8000/api/token-auth/`

## 5. Android Studio 실행

- 프로젝트 열기: `my-first-blog`
- 에뮬레이터 실행
- `UPLOAD_URL`은 이미 `http://10.0.2.2:8000/api_root/Post/`로 맞춰져 있음
- `Upload Photo` 버튼 클릭
- 권한 허용
- 이미지 선택

## 6. 결과 확인

- 안드로이드 토스트 메시지
- Django 메인 페이지 게시글 생성 여부
- Django admin 게시글 생성 여부
- API 목록/상세 응답

## 7. PythonAnywhere 반영

PythonAnywhere에서는 안드로이드 에뮬레이터용 `10.0.2.2` 대신 실제 도메인을 사용해야 합니다.
예:

```java
private static final String UPLOAD_URL = "https://tang0923.pythonanywhere.com/api_root/Post/";
```

그리고 배포 후:

```bash
python manage.py migrate
python manage.py collectstatic
```

마지막으로 Web 탭에서 `Reload`를 누릅니다.
