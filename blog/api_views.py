from django.utils import timezone
from rest_framework import generics, parsers, permissions, status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework.reverse import reverse

from .models import Post
from .serializers import PostSerializer


@api_view(["GET"])
@permission_classes([permissions.AllowAny])
def api_root(request, format=None):
    return Response(
        {
            "posts": reverse("api_post_list", request=request, format=format),
            "token_auth": reverse("api_token_auth", request=request, format=format),
        }
    )


class PostListCreateAPIView(generics.ListCreateAPIView):
    queryset = Post.objects.select_related("author").order_by(
        "-published_date",
        "-created_date",
        "-pk",
    )
    serializer_class = PostSerializer
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]
    parser_classes = [parsers.JSONParser, parsers.MultiPartParser, parsers.FormParser]

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)
        headers = self.get_success_headers(serializer.data)

        # The Android lecture code checks for HTTP 200 explicitly.
        return Response(serializer.data, status=status.HTTP_200_OK, headers=headers)

    def perform_create(self, serializer):
        serializer.save(
            author=self.request.user,
            created_date=serializer.validated_data.get("created_date") or timezone.now(),
            published_date=serializer.validated_data.get("published_date")
            or timezone.now(),
        )


class PostDetailAPIView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Post.objects.select_related("author").all()
    serializer_class = PostSerializer
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]
    parser_classes = [parsers.JSONParser, parsers.MultiPartParser, parsers.FormParser]

    def perform_update(self, serializer):
        save_kwargs = {}
        if self.request.user.is_authenticated:
            save_kwargs["author"] = self.request.user
        if (
            serializer.instance.published_date is None
            and "published_date" not in serializer.validated_data
        ):
            save_kwargs["published_date"] = timezone.now()

        serializer.save(**save_kwargs)
