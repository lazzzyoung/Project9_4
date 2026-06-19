from rest_framework import serializers

from .models import Post


DATETIME_INPUT_FORMATS = [
    "iso-8601",
    "%Y-%m-%d %H:%M:%S",
    "%Y-%m-%d %H:%M",
    "%Y-%m-%dT%H:%M:%S",
    "%Y-%m-%dT%H:%M:%S.%f",
    "%Y-%m-%dT%H:%M:%S%z",
    "%Y-%m-%dT%H:%M:%S.%f%z",
]


class PostSerializer(serializers.ModelSerializer):
    author = serializers.IntegerField(source="author_id", read_only=True)
    author_username = serializers.CharField(source="author.username", read_only=True)
    created_date = serializers.DateTimeField(
        required=False,
        input_formats=DATETIME_INPUT_FORMATS,
    )
    published_date = serializers.DateTimeField(
        required=False,
        allow_null=True,
        input_formats=DATETIME_INPUT_FORMATS,
    )
    image = serializers.ImageField(required=False, allow_null=True)
    image_url = serializers.SerializerMethodField()

    class Meta:
        model = Post
        fields = [
            "id",
            "author",
            "author_username",
            "title",
            "text",
            "created_date",
            "published_date",
            "image",
            "image_url",
        ]

    def to_internal_value(self, data):
        payload = data.copy() if hasattr(data, "copy") else dict(data)
        image_value = payload.get("image")

        # The lecture app sends a local file path string in JSON. Ignore it and
        # keep the default image unless a real uploaded file is provided.
        if image_value and not hasattr(image_value, "read"):
            payload.pop("image", None)

        return super().to_internal_value(payload)

    def get_image_url(self, obj):
        if not obj.image:
            return None

        request = self.context.get("request")
        if request is None:
            return obj.image.url

        return request.build_absolute_uri(obj.image.url)
