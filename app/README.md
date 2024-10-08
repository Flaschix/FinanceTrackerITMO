# FinanceTracker
## 1. Описание
    FinanceTracker — это мобильное приложение для отслеживания финансов. Оно позволяет пользователям управлять своими доходами и расходами, устанавливать бюджеты и анализировать финансовую активность. Приложение разработано для Android и использует современные технологии для обеспечения высокой производительности и удобства использования.

## 2. Системные требования
    Для разработки и запуска приложения "FinanceTracker" вам потребуются следующие инструменты и зависимости:

     - Операционная система: Windows 10/11, macOS, Linux
     - JDK: 1.8 (Java 8)
     - Android SDK: compileSdk 34, minSdk 26, targetSdk 34
     - Gradle: 8.10.2

### 3. Зависимости проекта

      - AndroidX Core KTX: 1.13.1
      -  JUnit: 4.13.2
      -  AndroidX JUnit: 1.2.1
      -  Espresso Core: 3.6.1
      -  AndroidX AppCompat: 1.7.0
      -  Google Material Components: 1.12.0
      -  AndroidX Activity: 1.9.2
      -  AndroidX ConstraintLayout: 2.1.4
      -  Navigation Fragment KTX: 2.8.1
      -  Navigation UI KTX: 2.8.1
      -  Hilt (Dagger Hilt): 2.48.1
      -  Room: 2.6.1
      -  MPAndroidChart: 3.1.0

## 4. Установка и настройка
   ### 4.1. Установка JDK
4.1.1 Скачайте и установите JDK 1.8 с официального сайта Oracle или используйте OpenJDK.

4.1.2 Установите переменную окружения `JAVA_HOME`: 
```sh
SETX JAVA_HOME "C:\Program Files\Java\jdk-1.8"
```

   ### 4.2. Установка Android SDK (если сборка приложения с использованием Gradle)
4.2.1 Скачайте и установите Android Studio.

4.2.2 Установите переменную окружения ANDROID_HOME: 
```sh 
SETX ANDROID_HOME "{path.to.android.sdk}"
```

   ### 4.3. Установка Gradle
4.3.1 Скачайте Gradle с официального сайта и распакуйте архив.

4.3.2 Установите переменную окружения GRADLE_HOME:
```sh
SETX GRADLE_HOME "C:\Gradle\gradle-8.10.2"
```

## 5. Клонирование репозитория
Клонируйте репозиторий: 
```sh
git clone https://github.com/Flaschix/FinanceTrackerITMO.git
```

## 6. Настройка проекта
Создайте файл local.properties в корне проекта:
```sdk.dir={path.to.sdk}```

## 7. Сборка приложения
   ### Вариант 1: Сборка приложения с использованием Gradle
7.1.1 Запустите сборку приложения:
```./gradlew clean assembleDebug```

7.1.2 После успешной сборки .apk файл будет создан в директории:
```<project_dir>/app/build/outputs/apk/debug/app-debug.apk```

   ### Вариант 2: Сборка приложения с использованием Jenkins
7.2.1 Создайте новый Jenkins Job.
7.2.2 Сборка приложения: в разделе "Build steps" напишите команду в "Tasks": 

для сборки отладочной версии приложения:
```./gradlew clean assembleDebug ```

для сборки всех конфигураций (debug и release):
```./gradlew clean assemble```        

7.2.3 Добавьте шаг для поиска и копирования .apk файла: в разделе "Build steps" нажмите на кнопку "Add build step". Выберите "Execute shell" (для Linux/macOS) или "Execute Windows batch command" (для Windows). Напишите команду:

Для Linux/macOS:
```sh
find $WORKSPACE -name "*.apk"
cp $WORKSPACE/FinanceTrackerITMO/app/build/outputs/apk/debug/*.apk $WORKSPACE/apk/
```
Для Windows:
```sh
dir /s /b %WORKSPACE%\*.apk
copy %WORKSPACE%\FinanceTrackerITMO\app\build\outputs\apk\debug\*.apk %WORKSPACE%\apk\
```
7.2.4 Архивируйте .apk файл: в разделе "Post-build Actions" нажмите на кнопку "Add post-build action". Выберите "Archive the artifacts". Напишите команду:
```apk/*.apk```

## 8. Запуск приложения
   ### Вариант 1. Запуск на эмуляторе
8.1.1 Запустите эмулятор Android.

8.1.2 Установите приложение на эмулятор:
```./gradlew installDebug```

   ### Вариант 2. Запуск на физическом устройстве

8.2.1 Установите приложение (файл *.apk) на устройство и запустите его

## Возможные проблемы и решения
    Ошибка 1: SDK location not found: Убедитесь, что переменная окружения ANDROID_HOME установлена и указывает на правильный путь.

    Ошибка 2: Gradle version mismatch: Убедитесь, что используется правильная версия Gradle.

## Контакты
Если у вас есть вопросы или предложения, пожалуйста, свяжитесь с нами по телеграму: 
    https://t.me/+GI5m4Ht9BdA0NTEy

## Заключение

Эта документация предоставляет полную информацию о проекте "FinanceTracker", включая системные требования, зависимости проекта, инструкции по установке и настройке, инструкции по сборке и запуску приложения.