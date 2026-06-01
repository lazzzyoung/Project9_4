# CH17 Assignment 10

실습 범위:

- Django 템플릿 동적 데이터
- CSS 분리
- 템플릿 확장
- 게시물 목록 / 상세
- 게시물 작성 / 수정

현재 루트 URL(`/`)은 ch17 실습용 블로그 화면으로 연결되어 있다.

## GitHub 업로드

이 프로젝트는 이미 Git 저장소로 초기화되어 있고 remote 도 연결되어 있다.

```bash
cd "/Users/kangtaeyeong/Desktop/2026/5-1/모바일웹서비스 프로그ᅢ밍/실습과제/week14_django_pythonanywhere/my-first-blog"
git add .
git commit -m "Complete ch17 Django template and CSS practice"
git push -u origin main
```

GitHub 제출 URL 예시:

```text
https://github.com/lazzzyoung/Project9_4
```

## PythonAnywhere 반영

PythonAnywhere Bash:

```bash
cd ~/my-first-blog
source venv/bin/activate
git pull origin main
python manage.py migrate
python manage.py createsuperuser
```

관리자 로그인 후:

- `/admin` 접속
- 게시물 1~2개 생성

그 다음 사이트 루트 확인:

```text
https://tang0923.pythonanywhere.com/
```

## Static 설정

PythonAnywhere Web 탭의 Static files:

- URL: `/static/`
- Directory: `/home/tang0923/my-first-blog/blog/static`

설정 후 `Reload`.

## 제출물

1. JPEG 캡처 이미지 1장
2. GitHub URL 1개
3. PythonAnywhere URL 1개

제출 예시:

- Image: `capture01.jpg`
- Text 1: `https://github.com/lazzzyoung/Project9_4`
- Text 2: `https://tang0923.pythonanywhere.com/`
