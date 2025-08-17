# GPS Tracker — Android Application

A full-featured Android application that enables users to **track their GPS location**, display latitude, longitude, calculate distance traveled, and identify frequently visited locations. Built with Java, `LocationManager`, and Android’s location services.

---

## **Key Features**

- **Real-Time Location Tracking:** Continuously fetches GPS coordinates.
- **Distance Calculation:** Computes total distance traveled dynamically.
- **Address Resolution:** Converts coordinates to human-readable addresses.
- **Favorite Location Detection:** Identifies locations where the user spends the most time.
- **Dynamic UI Updates:** Updates location, distance, and favorite location in real-time.
- **Permissions Handling:** Requests and manages location permissions.

---

## **Tech Stack**

| Layer     | Technology                           |
| --------- | ------------------------------------ |
| Platform  | Android                              |
| Language  | Java                                 |
| UI        | XML Layouts, TextView                |
| Location  | LocationManager, GPS_PROVIDER        |
| Permissions | Android Runtime Permissions        |

---

## **Project Structure**

- **MainActivity.java** — Main activity handling UI, GPS updates, distance calculations, and favorite location tracking.
- **res/layout/activity_main.xml** — Layout for the main activity including TextViews for latitude, longitude, distance, address, and favorite location.
- **AndroidManifest.xml** — App permissions for location and internet access.
- **build.gradle** — Project dependencies.

---

## **Quick Start**

1. Clone the project to your Android Studio workspace.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.
4. Grant location permissions when prompted.
5. Observe your **latitude, longitude, distance traveled**, and **favorite location** displayed on the UI.

---

## **Usage Notes**

- **Location Permissions:** The app requires `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION`.
- **Distance Calculation:** Uses successive GPS updates to calculate distance traveled in meters.
- **Address Display:** Converts GPS coordinates to human-readable addresses via `Geocoder`.
- **Favorite Location:** Tracks where the user spends the most time based on GPS updates.
- **Real-Time Updates:** UI updates every 5 seconds with the latest location and distance.

---

## **Future Improvements**

- Integrate persistent storage (SQLite or Room) to save location history.
- Add map visualization for the traveled route using Google Maps API.
- Enable notifications when entering frequently visited locations.
- Optimize GPS updates for battery efficiency.
- Implement multi-user support or cloud synchronization.
- Enhance UI with interactive maps and charts.

---

## **Acknowledgements**

This project demonstrates Android development skills including:

- Using `LocationManager` for real-time GPS tracking.
- Calculating distances between geographic coordinates.
- Managing runtime permissions in Android.
- Dynamically updating UI elements based on location changes.
- Implementing logic for identifying frequent user locations.

