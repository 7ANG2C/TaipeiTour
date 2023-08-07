![](../img/taipei_tour_api.png)

# Overview
- [架構](#架構)
- [使用](#使用)
- [備註](#備註)

## 架構
- 使用 retrofit 作為主要 request library
- 使用 koin 作為 dependency injection
- 取得原始資料後，會再進行一層轉接，進行資料的校正處理。例如:
  - scheme 中的 property 於 swagger 被定義為 non-nullable，而實際資料回傳 null
  - 原始命名不夠明確
  - ... 等情況
- 模組外層資料夾分為
  - internal - 僅供模組內部開發用途，不開放外部 access
  - external - 放置所有外部有機會 access 的資源，如：
    - 介面
    - 相對應的物件
    - 其他定義，如 object、module ... 等

## 使用
- Step1: 初始化資源
  - ```kotlin
      // 模組已有預設的介面實現，並且支援 override，以進行客製化需求
      TaipeiTourServiceModule.invoke(overrideModules = ...)
    ```
  - 如不使用 [TaipeiTourServiceModule] ，需自行實作對應所需的介面
- Step2: 使用 TaipeiTourApi
  - ```kotlin
      TaipeiTourApi.getAllAttractions(...)
    ```
  - 如不使用 [TaipeiTourApi] ，需自行實作對應所需的介面

## 備註
- [cover figma](https://www.figma.com/file/XZ3fJaUESt5pWt8JuAoxey/TaipeiTour?type=design&node-id=24%3A14&mode=design&t=SfYctFZVMhpSZBvl-1 "Figma")

[TaipeiTourApi]: ../TaipeiTourApi/src/main/java/com/module/taipeitourapi/external/data/TaipeiTourApi.kt
[TaipeiTourServiceModule]: ../TaipeiTourApi/src/main/java/com/module/taipeitourapi/external/di/TaipeiTourServiceModule.kt
