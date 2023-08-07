# TaipeiTour
- Hello Taipei, enjoy Taipei!
  ![](img/cover.png)

# Overview
- [專案介紹](#專案介紹)
- [主要畫面](#主要畫面)
- [程式碼檢查](#程式碼檢查)
- [開發環境](#開發環境)
- [REFERENCES](#REFERENCES)
- [備註](#備註)

## 專案介紹
- 使用 Jetpack Compose 進行介面開發
- 專案架構 MVVM 為主，部分參考：[Architecture Samples](https://github.com/android/architecture-samples "Android Architecture Samples")
- 使用 Flow 以及 coroutines 處理 asynchronous operations
- 使用 koin 作為 dependency injection
- 將 Open-API 的使用模組化[TaipeiTourApi](https://github.com/7ANG2C/TaipeiTour/tree/feature/prepare_readme/TaipeiTourApi "TaipeiTourApi")
- [PreviewFunctionFlavor] 在 debug/release 有不同實作，用以模擬區分 flavor/dimension 的開發方式
- 使用 [version catalogs](https://developer.android.com/build/migrate-to-catalogs "version catalogs") 搭配 [libs.versions.toml] 進行多模組版本管理

## 主要畫面

| 圖片                                                | 敘述                                                                                                            |
|---------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| <img src="img/main_screen.jpg" height="500">      | 主頁 MainScreen<br>- 卡片式列表 / 下拉刷新 / 上滑取更多<br/>- 具備 錯誤&無資料 的畫面反饋處理<br/>- 右下按鈕：向上滾 - 顯示當前進度<br/>- 右下按鈕：向下滾 - 一鍵滑至頂端 |
| <img src="img/attraction_screen.jpg" height="500"> | 單景點 AttractionFragment<br>- Fragment 內使用 ComposeView<br/>- Collapse Top Bar 動畫效果<br/>- 景點資訊如電話等，能夠點擊轉導至相關應用程式 |
| <img src="img/web_into_screen.jpg" height="500">  | 網頁景點 WebIntroductionScreen<br>- Uri 轉導處理<br/>- 可供下載檔案 (DownloadManager)<br/>- 位置權限請求處理                        |
| <img src="img/setting_screen.jpg" height="500">   | 設定 SettingScreen <br>- 更換語系時有 slide-fade 效果<br/>- 提供 DarkMode Switch<br/>- Preview Function 在 debug 時提供更多設置   |
| <img src="img/language_dialog.jpg" height="500">  | 多語系彈窗<br>                                                                                                     |
| <img src="img/drawer.jpg" height="500">           | 側邊欄<br>- 切換功能時，主畫面有 scale 效果                                                                                  |
- 使用 DataStore 取代 SharedPreferences 進行資料永久儲存 
- 使用 [coil](https://developer.android.com/jetpack/compose/graphics/images/loading#coil "coil") 進行圖片讀取

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

## REFERENCES
- [Travel-Taipei Open API](https://www.travel.taipei/open-api/swagger/ui/index#/ "travel-taipei-open-api")
- [Material3 Theme Builder](https://m3.material.io/theme-builder#/custom "md3-theme-builder")

## 備註
- [設計稿](https://www.figma.com/file/XZ3fJaUESt5pWt8JuAoxey/TaipeiTour?type=design&node-id=0-1&mode=design "Figma")
- [Demo](https://www.youtube.com/watch?v=i3aD9Is7a2U "Youtube")
  - 手機規格 : SAMSUNG Galaxy Note 10+ (Android 12)
- 時程
  | 0727 | 0728 | 0729 | 0730 | 0731 | 0801 | 0802 |
  | - | - | - | - | - | - | - |
  | 確認規格<br>規劃實作方式 | 專案建置<br>服務串接<br>ImageSlider 實作 | 主頁實作<br>DataStore 建置<br>Flavor 建置 | 研究動畫<br>依賴版本更新 | Drawer 實作<br>單景點頁面實作<br>部分頁面動畫實作 | 單景點頁面實作<br>UserPreferences 建置 | 完善主頁資料流 |
  | 0803 | 0804 | 0805 | 0806 | 0807 | - | - |
  | 設定頁實作<br>國旗刻劃 | WebView實作<br>研究地圖渲染 | 測試研究與撰寫 | 專案審視整理<br>Splash 畫面 | 文件撰寫與整理<br>影片錄製 | - | - |
- 目前 `GET/{lang}/Attractions/All` 在語系為印尼時，結果為 fail (最後測試時間為 2023-08-07)
  ![](img/request_fail_lang_id.png)

[PreviewFunctionFlavor]: ./app/src/main/java/com/fang/taipeitour/flavor/PreviewFunctionFlavor.kt
[libs.versions.toml]: ./gradle/libs.versions.toml
