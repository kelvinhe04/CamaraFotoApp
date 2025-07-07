# 📸 Camera Photo App

An Android application that allows users to take photos, save them, and view them in a gallery using private external storage.

## ✨ Features

- 📷 Take photos using the device camera
- 💾 Save photos to private external storage
- 🖼️ View saved photos in a grid gallery
- 🗑️ Delete all saved photos with one click
- 🔒 Uses `FileProvider` for secure file sharing

## 📁 Storage

Photos are saved in the app’s private external storage directory:

- Temporary photos are named using the prefix `TEMP_`
- Saved photos are renamed with the prefix `FOTO_`

## 🚀 How to Use

1. **Launch the app**
2. Tap **Take Photo**
3. Once photo is previewed, tap **Save Photo** to keep it
4. Tap **View Gallery** to browse saved images
5. Tap **Delete Photos** to clear the gallery

## 🛡️ Permissions

- Camera permission is requested at runtime
- No internet connection is required
- Photos are stored securely and not shared externally

## 🧱 Tech Stack

- Java
- Android SDK
- `RecyclerView` for gallery display
- `FileProvider` for secure image capture

## 📸 Screenshots

> _You can include screenshots here if available_

## 📂 Project Structure

- `MainActivity.java` - Handles photo capture and saving
- `GaleriaActivity.java` - Displays photos in a grid gallery
- `GaleriaAdapter.java` - Adapter for `RecyclerView`
- `GridSpacingItemDecoration.java` - Adds spacing to grid items

## 📌 Note

- Photos are stored **privately** in app-specific storage. They are **not accessible** from the device gallery.
- Works with Android 10 and above (Scoped Storage compatible)

## 🧑‍💻 Author

Created by kelvinhe04

## 📃 License

This project is licensed under the MIT License.
