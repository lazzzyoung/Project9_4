from django.urls import path
from rest_framework.authtoken.views import obtain_auth_token
from rest_framework.urls import urlpatterns as rest_framework_urlpatterns

from .api_views import PostDetailAPIView, PostListCreateAPIView, api_root
from . import views


urlpatterns = [
    path("", views.post_list, name="post_list"),
    path("post/<int:pk>/", views.post_detail, name="post_detail"),
    path("post/new/", views.post_new, name="post_new"),
    path("post/<int:pk>/edit/", views.post_edit, name="post_edit"),
    path("assignment09/", views.assignment09, name="assignment09"),
    path("js-test/", views.js_test, name="js_test"),
    path("api_root/", api_root, name="api_root"),
    path("api_root/Post/", PostListCreateAPIView.as_view(), name="api_post_list"),
    path(
        "api_root/Post/<int:pk>/",
        PostDetailAPIView.as_view(),
        name="api_post_detail",
    ),
    path("api/token-auth/", obtain_auth_token, name="api_token_auth"),
]

urlpatterns += rest_framework_urlpatterns
