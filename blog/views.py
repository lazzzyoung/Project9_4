from django.shortcuts import get_object_or_404, redirect, render
from django.utils import timezone

from .forms import PostForm
from .models import Post


def post_list(request):
    posts = Post.objects.filter(published_date__lte=timezone.now()).order_by("published_date")
    return render(request, "blog/post_list.html", {"posts": posts})


def post_detail(request, pk):
    post = get_object_or_404(Post, pk=pk)
    return render(request, "blog/post_detail.html", {"post": post})


def post_new(request):
    if request.method == "POST":
        form = PostForm(request.POST, request.FILES)
        if form.is_valid():
            post = form.save(commit=False)
            post.author = request.user
            post.published_date = timezone.now()
            post.save()
            return redirect("post_detail", pk=post.pk)
    else:
        form = PostForm()

    return render(request, "blog/post_edit.html", {"form": form})


def post_edit(request, pk):
    post = get_object_or_404(Post, pk=pk)
    if request.method == "POST":
        form = PostForm(request.POST, request.FILES, instance=post)
        if form.is_valid():
            post = form.save(commit=False)
            post.author = request.user
            post.published_date = timezone.now()
            post.save()
            return redirect("post_detail", pk=post.pk)
    else:
        form = PostForm(instance=post)

    return render(request, "blog/post_edit.html", {"form": form})


def assignment09(request):
    context = {
        "poem_title": "자화상",
        "poet": "윤동주",
        "poem_lines": [
            "산모퉁이를 돌아 논가 외딴 우물을 홀로 찾아가선 가만히 들여다봅니다.",
            "우물 속에는 달이 밝고 구름이 흐르고 하늘이 펼치고 파아란 바람이 불고 가을이 있습니다.",
            "그리고 한 사나이가 있습니다.",
            "어쩐지 그 사나이가 미워져 돌아갑니다.",
            "돌아가다 생각하니 그 사나이가 가엾어집니다. 도로 가 들여다보니 사나이는 그대로 있습니다.",
            "다시 그 사나이가 미워져 돌아갑니다.",
            "돌아가다 생각하니 그 사나이가 그리워집니다.",
            "우물 속에는 달이 밝고 구름이 흐르고 하늘이 펼치고 파아란 바람이 불고 가을이 있고 추억처럼 사나이가 있습니다.",
        ],
        "other_poems": [
            "서시",
            "자화상",
            "눈 오는 지도",
            "새로운 길",
            "길",
            "별 헤는 밤",
            "쉽게 씌어진 시",
            "참회록",
            "못 자는 밤",
            "비오는 밤",
            "소낙비",
        ],
        "audio_src": "https://interactive-examples.mdn.mozilla.net/media/cc0-audio/t-rex-roar.mp3",
        "video_mp4": "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
        "video_webm": "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.webm",
    }
    return render(request, "blog/assignment09.html", context)
