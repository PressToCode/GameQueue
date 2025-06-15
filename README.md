# GameQueue - Sistem Reservasi Game Console

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Language">
  <img src="https://img.shields.io/badge/SDK-30+-blue.svg" alt="SDK">
  <img src="https://img.shields.io/badge/Database-Firebase-yellow.svg" alt="Database">
</div>

## 📋 Deskripsi

GameQueue adalah aplikasi Android untuk sistem reservasi game console yang memungkinkan pengguna untuk mereservasi waktu bermain pada berbagai konsol game seperti PlayStation, Xbox, PC Gaming, Nintendo Switch, dan Steam Deck. Aplikasi ini dilengkapi dengan panel admin untuk mengelola permintaan reservasi.

## ✨ Fitur Utama

### 👤 Untuk Pengguna
- **Autentikasi** - Login/Register dengan email/password atau Google Sign-in
- **Browsing Console** - Melihat daftar konsol game yang tersedia
- **Sistem Reservasi** - Mereservasi console dengan jadwal dan waktu tertentu
- **Multi-Step Form** - Proses reservasi 3 langkah (Jadwal → Identitas → Dokumen)
- **Status Tracking** - Melihat status reservasi (Pending, Approved, Rejected, Completed)
- **History** - Riwayat reservasi sebelumnya
- **Profile Management** - Mengelola profil dan mengubah password

### 👨‍💼 Untuk Admin
- **Dashboard Admin** - Panel khusus untuk mengelola reservasi
- **Review Requests** - Mereview dan approve/reject permintaan reservasi
- **Console Management** - Mengelola ketersediaan konsol
- **Real-time Updates** - Notifikasi real-time untuk permintaan baru

## 🛠️ Tech Stack

- **Platform**: Android (API 30+)
- **Language**: Java
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Firebase Realtime Database
- **Authentication**: Firebase Auth
- **UI Components**: Material Design Components
- **Image Handling**: ImagePicker Library
- **Navigation**: Bottom Navigation, ViewPager

## 🏗️ Arsitektur Aplikasi

```
app/
├── data/
│   ├── firebase/          # Firebase utilities
│   ├── model/            # Data models
│   ├── repository/       # Data repositories
│   └── sharedViewModel/  # Shared ViewModels
├── ui/
│   ├── main/            # Activities
│   ├── fragment/        # Fragments
│   └── adapter/         # RecyclerView adapters
├── utils/               # Utility classes
└── widgets/             # Custom widgets
```

## 📱 Screenshots & Fitur Detail

### 🔐 Autentikasi
- Login dengan email/password
- Register akun baru
- Google Sign-in integration
- Auto-login untuk session yang masih aktif

### 🎮 Console yang Tersedia
- **PlayStation** (PS4 Pro, PS5, PS5 Digital)
- **Xbox** (Series X, Series S)
- **PC Gaming** (Alienware Aurora, Custom Gaming Rig)
- **Nintendo Switch** (OLED, Lite)
- **Steam Deck**

### 📅 Sistem Reservasi
1. **Pilih Jadwal**: Memilih hari (Senin-Jumat) dan waktu (09:30-16:00)
2. **Input Identitas**: Nama, NIM, No. Telpon, Program Studi
3. **Upload Dokumen**: KTP/KTM sebagai verifikasi identitas

### 📊 Status Reservasi
- **Pending**: Menunggu review admin
- **Approved**: Disetujui dengan kode verifikasi
- **Rejected**: Ditolak oleh admin
- **Completed**: Selesai menggunakan console
- **Canceled**: Dibatalkan pengguna

## 🔧 Setup & Installation

### Prerequisites
- Android Studio Arctic Fox atau lebih baru
- Android SDK 30+
- Firebase project

### Langkah Instalasi

1. **Clone Repository**
   ```bash
   git clone https://github.com/your-username/gamequeue.git
   cd gamequeue
   ```

2. **Setup Firebase**
    - Buat project di [Firebase Console](https://console.firebase.google.com)
    - Aktifkan Authentication (Email/Password & Google)
    - Aktifkan Realtime Database
    - Download `google-services.json` ke folder `app/`

3. **Konfigurasi Database**
   ```
   Firebase Realtime Database Structure:
   ├── consoles/
   ├── reservations/
   ├── requests/
   └── admins/
   ```

4. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```

## 🔥 Firebase Configuration

### Database Rules
```json
{
  "rules": {
    "consoles": {
      ".read": "auth != null",
      ".write": "auth != null"
    },
    "reservations": {
      "$uid": {
        ".read": "auth.uid == $uid",
        ".write": "auth.uid == $uid"
      }
    },
    "requests": {
      ".read": "auth != null",
      ".write": "auth != null"
    },
    "admins": {
      ".read": "auth != null"
    }
  }
}
```

### Database Structure
```
gamequeue-db/
├── consoles/
│   └── [consoleId]/
│       ├── title: "PlayStation 5"
│       ├── specificationOne: "4K HDR Gaming"
│       ├── specificationTwo: "120Hz Refresh Rate"
│       ├── specificationThree: "DualSense Controller"
│       ├── lendingStatus: false
│       └── lenderUid: ""
├── reservations/
│   └── [userId]/
│       └── [reservationId]/
│           ├── consoleId: ""
│           ├── consoleName: ""
│           ├── date: "2024-01-15"
│           ├── time: "10:30"
│           ├── lenderName: ""
│           ├── lenderEmail: ""
│           ├── lenderPhone: ""
│           ├── lenderNIM: ""
│           ├── lenderProdi: ""
│           ├── status: "pending"
│           └── verificationCode: ""
├── requests/
│   └── [reservationId]/
│       ├── userId: ""
│       └── consoleId: ""
└── admins/
    └── [adminUserId]: true
```

## 👥 User Roles

### Regular User
- Dapat melihat console yang tersedia
- Dapat membuat reservasi
- Dapat melihat status dan history reservasi
- Dapat mengelola profil

### Admin
- Dapat mereview semua permintaan reservasi
- Dapat approve/reject reservasi
- Dapat melihat semua data reservasi
- Akses ke dashboard admin

## 🔒 Security Features

- **Authentication**: Firebase Auth dengan validasi email
- **Authorization**: Role-based access (User/Admin)
- **Data Validation**: Input validation pada semua form
- **Secure Storage**: Data tersimpan di Firebase dengan security rules
- **Session Management**: Auto-logout dan session handling

## 📝 Development Notes

### Important Classes

#### Activities
- `MainActivity` - Main container dengan bottom navigation
- `AuthActivity` - Authentication flow
- `ReservationProcessActivity` - Multi-step reservation process
- `AdminActivity` - Admin dashboard
- `ReservationDetailActivity` - Detail reservasi dengan status tracking

#### ViewModels
- `ConsoleSharedViewModel` - Manage console data
- `ReservationSharedViewModel` - Manage user reservations
- `RequestSharedViewModel` - Manage admin requests
- `ReservationFormSharedViewModel` - Handle reservation form data

#### Key Features
- **Real-time Updates**: LiveData dengan Firebase listeners
- **Offline Support**: Firebase persistence enabled
- **Image Upload**: ImagePicker dengan compression
- **Multi-step Forms**: ViewPager dengan fragment navigation
- **Status Tracking**: Real-time status updates


## 📞 Contact

- **Developer**:
<ul>
  <li>Muhammad Olfat Faiz</li>
  <li>Ketut Bagus Wedanta Ananda Murti</li>
   <li>Pande Gede Natha Satvika</li>
</ul>
- **Project Link**: https://github.com/PressToCode/GameQueue

---

<div align="center">
Made with ❤️ for Olfat, Wedanta, and Natha
</div>