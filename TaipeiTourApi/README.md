# TaipeiTourApi
- Taipei x Swagger
  ![](../img/taipei_tour_api.png)

# Overview
- [程式碼檢查](#程式碼檢查)
- [開發環境](#開發環境)
- [備註](#備註)

## QuickStart

- Initialization
- Usage

## Initialization

- TaipeiTourServiceModule

## Usage

- TaipeiTourApi.

  ```kotlin
  TaipeiTourApi
  ```

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

## 備註
- [設計稿](https://www.figma.com/file/XZ3fJaUESt5pWt8JuAoxey/TaipeiTour?type=design&node-id=24%3A14&mode=design&t=SfYctFZVMhpSZBvl-1 "Figma")

[TaipeiTourApi]: ../TaipeiTourApi/src/main/java/com/module/taipeitourapi/external/data/TaipeiTourApi.kt
[TaipeiTourServiceModule]: ../TaipeiTourApi/src/main/java/com/module/taipeitourapi/external/di/TaipeiTourServiceModule.kt
