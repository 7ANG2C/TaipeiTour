# TaipeiTour
Hello Taipei, enjoy Taipei!

# Overview
- 規劃文件
- 建置環境
- 專案架構
- 程式碼檢查

## [規劃文件](https://docs.google.com/spreadsheets/d/1QpEoIRHtYI7vEpig8LWy-YfU5gadsuVDNbBDz73-r6g/edit#gid=0 "DOC")

## 專案架構
- 使用 Kotlin 開發
- MVVM [部分參考](https://github.com/android/architecture-samples "Android Architecture Samples")
- 使用 Flow 以及 coroutines 處理 asynchronous operations
- 使用 koin 作為 dependency injection
- debug/release flavor 可以提供不同實作

## Third-Party
- [coil](https://developer.android.com/jetpack/compose/graphics/images/loading#internet-loading "coil")

## CI/CD

## 程式碼檢查
- 每個 commit 檢查
- 使用 [spotless](https://github.com/diffplug/spotless "spotless") 檢查 code style
    - `./gradlew spotlessCheck` 檢查程式碼
    - `./gradlew spotlessApply` 修正程式碼

## 建置環境
- Android Studio Giraffe | 2022.3.1
- Build #AI-223.8836.35.2231.10406996, built on June 29, 2023
- Runtime version: 17.0.6+0-17.0.6b829.9-10027231 aarch64
- VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
- macOS 12.6 | Memory: 4096M | Cores: 8

## 設計理念
- 版面參考各大旅遊 APP
- Style 參考國泰