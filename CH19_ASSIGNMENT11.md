# CH19 Assignment 11

실습 범위:

- `blog/templates/blog/js_test.html` 버그 수정
- 게임 과정 캡처 3장 이상
- 수정된 HTML 제출

## 수정한 주요 버그

- `Math.floor(Math.random()) + 1` -> `Math.floor(Math.random() * 100) + 1`
- `document.querySelector('lowOrHi')` -> `document.querySelector('.lowOrHi')`
- `elseif` -> `else if`
- `guessSubmit.addeventListener` -> `guessSubmit.addEventListener`
- `functionsetGameOver()` -> `function setGameOver()`
- `document.querySelectorAll('.resultParasp')` -> `document.querySelectorAll('.resultParas p')`
- `margin: 0auto;` -> `margin: 0 auto;`

## 경로

수정된 HTML:

`blog/templates/blog/js_test.html`

브라우저 경로:

`/js-test/`

예:

`https://tang0923.pythonanywhere.com/js-test/`

## PythonAnywhere 반영

```bash
cd ~/my-first-blog
source venv/bin/activate
git fetch origin
git reset --hard origin/main
python manage.py migrate
```

그 다음 `Web` 탭에서 `Reload`.

## 캡처 추천

1. 첫 화면
2. 오답 입력 후 `too low` 또는 `too high`가 보이는 화면
3. 정답을 맞춘 화면 또는 `GAME OVER` 화면
