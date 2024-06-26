# ToDoApp - Documentație 📋

---

### Acces la GIT 🔗
Proiectul este disponibil pe GIT: [ToDoApp-ANDROID](https://github.com/Vlazzzz/ToDoApp-ANDROID)

---

### Activități, Fragmente & Navigație 🧭
#### Activitate Principală
`MainActivity` conține un `NavHostFragment` pentru navigare.

#### Fragmente
- **SignInFragment**: Formular de autentificare.
- **SignUpFragment**: Formular de înregistrare.
- **HomeFragment**: Afișează sarcinile utilizatorului.
- **SplashFragment**: Primul ecran afișat la deschiderea aplicației.

#### NavController
Folosit pentru navigarea între fragmente.

---

### Autentificare 🔐
#### Înregistrare, Autentificare
Utilizatorii își pot crea conturi noi și se pot autentifica în aplicație.

#### Persistența Autentificării
Firebase păstrează utilizatorii autentificați chiar și după închiderea aplicației.

---

### Baza de Date & RecyclerView 📊
#### Firebase Realtime Database
Sarcinile utilizatorilor sunt stocate și preluate din Firebase.

#### RecyclerView
Sarcinile sunt afișate folosind un adapter personalizat.

---

### SharedPreferences 🗄️
Datele salvate sunt utilizate pentru autentificarea automată la deschiderea aplicației.

---

### Aplicație User-Friendly 😊
#### Design Responsiv
Elementele UI sunt accesibile pe diferite dimensiuni de ecran.

#### Interfață Intuitivă
Butoanele și câmpurile sunt clar etichetate și poziționate logic.

---

### Concluzie 🏁
ToDoApp oferă gestionarea eficientă a sarcinilor, utilizând Firebase pentru autentificare și stocare a datelor, SharedPreferences pentru păstrarea informațiilor de autentificare și un design prietenos pentru utilizator.
