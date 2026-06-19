from rest_framework.authentication import TokenAuthentication, get_authorization_header
from rest_framework.exceptions import AuthenticationFailed


class LegacyKeywordTokenAuthentication(TokenAuthentication):
    """Accept legacy lecture headers such as `Authorization: JWT <token>`."""

    keyword = "JWT"
    valid_keywords = {b"jwt", b"token"}

    def authenticate(self, request):
        auth = get_authorization_header(request).split()

        if not auth or auth[0].lower() not in self.valid_keywords:
            return None

        if len(auth) == 1:
            raise AuthenticationFailed("Invalid token header. No credentials provided.")

        if len(auth) > 2:
            raise AuthenticationFailed(
                "Invalid token header. Token string should not contain spaces."
            )

        try:
            token = auth[1].decode()
        except UnicodeError as exc:
            raise AuthenticationFailed(
                "Invalid token header. Token string contains invalid characters."
            ) from exc

        return self.authenticate_credentials(token)
