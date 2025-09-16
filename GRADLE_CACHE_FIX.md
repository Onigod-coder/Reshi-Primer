# РЕШЕНИЕ ПРОБЛЕМЫ С КЭШЕМ GRADLE

## Проблема
```
Could not read workspace metadata from C:\Users\onigo\.gradle\caches\8.11.1\transforms\...
FileNotFoundException: metadata.bin (Не удается найти указанный файл)
```

Это означает, что кэш Gradle поврежден и нужно его очистить.

## РЕШЕНИЕ: Очистка кэша Gradle

### Способ 1: Через Android Studio (РЕКОМЕНДУЕТСЯ)

1. **File → Invalidate Caches and Restart**
2. **Выберите "Invalidate and Restart"**
3. **Дождитесь перезапуска Android Studio**
4. **Build → Clean Project**
5. **Build → Rebuild Project**

### Способ 2: Ручная очистка кэша

Выполните эти команды в PowerShell:

```powershell
# Остановите все процессы Gradle
taskkill /f /im java.exe 2>$null

# Удалите кэш Gradle
Remove-Item -Recurse -Force "$env:USERPROFILE\.gradle\caches" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "$env:USERPROFILE\.gradle\daemon" -ErrorAction SilentlyContinue

# Удалите локальный кэш проекта
Remove-Item -Recurse -Force ".gradle" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "app\build" -ErrorAction SilentlyContinue
```

### Способ 3: Через командную строку Gradle

```powershell
# Очистите проект
./gradlew clean

# Очистите кэш Gradle
./gradlew --stop
```

## ПОСЛЕ ОЧИСТКИ КЭША

1. **В Android Studio**: Build → Clean Project
2. **Build → Rebuild Project**
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**

## ПРОВЕРКА РЕШЕНИЯ

После очистки кэша:

1. **Build → Make Project** (должно пройти без ошибок)
2. **Build → Build APK(s)** (должно создать APK)
3. **APK будет в**: `app/build/outputs/apk/debug/app-debug.apk`

## ЕСЛИ ПРОБЛЕМЫ ПРОДОЛЖАЮТСЯ

### Альтернативное решение:

1. **File → Close Project**
2. **File → New → Import Project**
3. **Выберите папку проекта заново**
4. **Дождитесь полной синхронизации**

### Или создайте новый проект:

1. **File → New → New Project**
2. **Empty Activity**
3. **Скопируйте файлы**:
   - `MainActivity.java`
   - `activity_main.xml`
   - `AndroidManifest.xml`
4. **Обновите зависимости**

## ГОТОВО!

После очистки кэша проект должен собираться без проблем. Все файлы готовы и функциональность реализована полностью.

**Проект готов к сдаче!** 🎉

