# TaipeiTour
- Hello Taipei, enjoy Taipei!
  ![](./doc/cover.png)

# Overview
- [專案架構](#專案架構)
- [程式碼檢查](#程式碼檢查)
- 備註錄影機型
- 規劃文件
- 建置環境

## 專案架構
- 使用 Jetpack Compose 進行介面開發
- 專案架構 MVVM 為主 [部分參考](https://github.com/android/architecture-samples "Android Architecture Samples")
- 使用 Flow 以及 coroutines 處理 asynchronous operations
- 使用 koin 作為 dependency injection
- debug/release flavor 可以提供不同實作
- [模組化](https://github.com/7ANG2C/TaipeiTour/tree/feature/prepare_readme/TaipeiTourApi "TaipeiTourApi")

## 畫面重點
- Main
- 主頁
  - guide - 使用 fragment
  - 網頁 - 做了啥處理??
- setting
- 備註 目前多語系的部分，id是錯的

## 程式碼檢查
- 使用 [spotless](https://github.com/diffplug/spotless "spotless") 檢查 code style
    - `./gradlew spotlessCheck` 檢查程式碼
    - `./gradlew spotlessApply` 修正程式碼

## 開發環境
- Android Studio Giraffe | 2022.3.1
- Build #AI-223.8836.35.2231.10406996, built on June 29, 2023
- Runtime version: 17.0.6+0-17.0.6b829.9-10027231 aarch64
- VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
- macOS 12.6 | Memory: 4096M | Cores: 8

## 手機 Demo 環境
- SAMSUNG Galaxy Note 10+
- Android 12

## References
- [Travel-Taipei Open API](https://www.travel.taipei/open-api/swagger/ui/index#/ "travel-taipei-open-api")
- [Material3 Theme Builder](https://m3.material.io/theme-builder#/custom "md3-theme-builder")

## 備註
- [設計稿](https://www.figma.com/file/XZ3fJaUESt5pWt8JuAoxey/TaipeiTour?type=design&node-id=0-1&mode=design "Figma")
- [Demo](https://www.figma.com/file/XZ3fJaUESt5pWt8JuAoxey/TaipeiTour?type=design&node-id=0-1&mode=design "Youtube")
- 估時
  | 一 | 二 | 二 | 二 | 二 | 二 | 二 | 二 |
  | - | - | - | - | - | - | - | - |
  | 一 | 二 | 二 | 二 | 二 | 二 | 二 | 二 |
  | 一 | 二 | 二 | 二 | 二 | 二 | 二 | 二 |
