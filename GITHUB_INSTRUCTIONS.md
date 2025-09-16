# Инструкции по загрузке проекта на GitHub

## Способ 1: Через веб-интерфейс GitHub

1. Перейдите на https://github.com
2. Войдите в свой аккаунт GitHub
3. Нажмите кнопку "New repository" (зеленая кнопка)
4. Заполните поля:
   - **Repository name**: `reshiprimer`
   - **Description**: `Android Math Quiz App - приложение для решения математических примеров`
   - Выберите **Public** или **Private** (по желанию)
   - **НЕ** добавляйте README, .gitignore или лицензию (они уже есть в проекте)
5. Нажмите "Create repository"

6. После создания репозитория выполните следующие команды в терминале:

```bash
git remote add origin https://github.com/YOUR_USERNAME/reshiprimer.git
git branch -M main
git push -u origin main
```

Замените `YOUR_USERNAME` на ваш GitHub username.

## Способ 2: Установка GitHub CLI (рекомендуется)

1. Скачайте GitHub CLI с https://cli.github.com/
2. Установите его
3. Выполните команды:

```bash
gh auth login
gh repo create reshiprimer --public --description "Android Math Quiz App - приложение для решения математических примеров"
git remote add origin https://github.com/YOUR_USERNAME/reshiprimer.git
git branch -M main
git push -u origin main
```

## После загрузки

После успешной загрузки проекта на GitHub, ссылка на репозиторий будет:
`https://github.com/YOUR_USERNAME/reshiprimer`

## Проверка проекта

Убедитесь, что в репозитории есть все необходимые файлы:
- ✅ MainActivity.java
- ✅ activity_main.xml
- ✅ AndroidManifest.xml
- ✅ build.gradle файлы
- ✅ README.md
- ✅ Все ресурсы приложения

## Запуск проекта

1. Клонируйте репозиторий на другой машине:
   ```bash
   git clone https://github.com/YOUR_USERNAME/reshiprimer.git
   ```

2. Откройте проект в Android Studio
3. Синхронизируйте Gradle
4. Запустите приложение


