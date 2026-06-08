# CH18 Assignment 12

실습 범위:

- `Post` 모델에 이미지 필드 추가
- `MEDIA_URL`, `MEDIA_ROOT` 추가
- 템플릿에서 이미지 출력
- CSS로 이미지 스타일 지정

## 로컬 반영 후 GitHub 업로드

```bash
cd "/Users/kangtaeyeong/Desktop/2026/5-1/모바일웹서비스 프로그ᅢ밍/실습과제/week14_django_pythonanywhere/my-first-blog"
git add .
git commit -m "Complete ch18 image blog practice"
git push -u origin main
```

## PythonAnywhere

```bash
cd ~/my-first-blog
source venv/bin/activate
pip install Pillow
git fetch origin
git reset --hard origin/main
python manage.py migrate
```

Static files:

- URL: `/static/`
- Directory: `/home/tang0923/my-first-blog/blog/static`

Media files:

- URL: `/media/`
- Directory: `/home/tang0923/my-first-blog/media`

그 다음 `Web` 탭에서 `Reload`.

## 실습 확인 순서

1. `/admin` 로그인
2. 게시글 추가 또는 수정
3. 이미지 업로드
4. 메인 화면에서 이미지 출력 확인
5. 업로드 파일이 `media/intruder_image/...` 아래 생성됐는지 확인

## 제출물

1. 단계별 결과 이미지 캡처
2. GitHub URL
3. PythonAnywhere URL
4. 두 URL을 적은 text 파일

예시:

```text
https://github.com/lazzzyoung/Project9_4
https://tang0923.pythonanywhere.com/
```
