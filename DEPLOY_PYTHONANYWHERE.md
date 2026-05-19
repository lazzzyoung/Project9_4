# PythonAnywhere Deployment Notes

This project matches the lecture flow around page 45:

- project root: `my-first-blog`
- Django project: `mysite`
- app: `blog`
- template: `blog/templates/blog/post_list.html`

## 1. Upload or clone the project

Use one of the following:

- upload this folder manually in the PythonAnywhere Files tab
- or push to GitHub locally, then clone on PythonAnywhere

```bash
git clone https://github.com/<your-github-username>/my-first-blog.git
cd my-first-blog
```

## 2. Create and activate a virtualenv

Pick the same Python version in both the web app and the virtualenv.

```bash
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

## 3. Initialize the database

```bash
python manage.py migrate
```

## 4. Create the web app

In PythonAnywhere:

1. Web tab
2. Add a new web app
3. Choose your free domain
4. Select `Manual configuration`
5. Choose a Python version

## 5. Set the virtualenv path

Web tab -> Virtualenv

Example:

```text
/home/<your-username>/my-first-blog/venv
```

## 6. Edit the WSGI file

Open:

```text
/var/www/<your-username>_pythonanywhere_com_wsgi.py
```

Replace its contents with:

```python
import os
import sys

path = "/home/<your-username>/my-first-blog"
if path not in sys.path:
    sys.path.append(path)

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "mysite.settings")

from django.core.wsgi import get_wsgi_application

application = get_wsgi_application()
```

If your account is on the EU system, the filename will usually contain `_eu_pythonanywhere_com_wsgi.py`.

## 7. Reload and verify

Click `Reload` in the Web tab, then open your personal URL:

- `https://<your-username>.pythonanywhere.com`
- or `https://<your-username>.eu.pythonanywhere.com`

## 8. Submit

Submit these two items:

1. your personal PythonAnywhere URL
2. a screenshot showing the page rendered from that URL
