# HabitFlow 🌿

HabitFlow es una aplicación móvil nativa para Android diseñada para ayudar a los usuarios a desarrollar hábitos saludables mediante el seguimiento de rutinas, establecimiento de objetivos y visualización del progreso diario.

La aplicación permite gestionar hábitos personales de forma sencilla, incluso sin conexión a internet, gracias a un sistema de persistencia híbrida que sincroniza los datos con la nube cuando la conexión está disponible.

---

## 📌 Proyecto Académico

Este repositorio contiene el desarrollo de **HabitFlow**, una aplicación móvil Android enfocada en la gestión de hábitos saludables.

El proyecto fue desarrollado con el objetivo de aplicar buenas prácticas de desarrollo de software, utilizando **arquitectura moderna (MVVM)**, persistencia local mediante **Room Database** y sincronización con servicios en la nube utilizando **Supabase**.

El repositorio incluye:

- Descripción del sistema
- Diagrama de base de datos (DER)
- Capturas de pantalla de la aplicación
- Tecnologías utilizadas
- Estructura del proyecto

---

## ✨ Características

- Gestión completa de hábitos (Crear, leer, actualizar y eliminar)
- Sistema de objetivos y metas personalizadas
- Registro histórico del cumplimiento de hábitos
- Funcionamiento offline utilizando Room Database
- Sincronización automática con base de datos en la nube
- Recordatorios y notificaciones de hábitos
- Autenticación segura de usuarios mediante Supabase

---

## 📱 Capturas de Pantalla

| Inicio de Sesión | Dashboard de Hábitos | Estadísticas |
|------------------|----------------------|--------------|
| ![](images/login.jpeg) | ![](images/habitos.jpeg) | ![](images/estadisticas.jpeg) |

---

## 📊 Diagrama de Entidad-Relación (DER)

El modelo de datos está diseñado para mantener la integridad entre los usuarios, sus hábitos y los registros de cumplimiento.

![DER HabitFlow](images/DER_HabitFlow.png)

### Descripción de Entidades

#### usuarios

Almacena la información básica del usuario.

Campos principales:

- id
- nombre
- email
- password
- foto

#### habitos

Define los hábitos creados por cada usuario.

Campos principales:

- id
- nombre
- hora
- categoriaId
- usuarioEmail

#### registro_habitos

Tabla histórica que registra si un hábito fue completado en una fecha específica.

Campos principales:

- id
- habitoId
- fecha
- completado

#### objetivos

Gestiona metas asociadas a hábitos y permite medir el progreso.

Campos principales:

- id
- metaValor
- valorActual
- habitoId

#### categorias

Permite clasificar los hábitos para una mejor organización.

Campos principales:

- id
- nombre

---

## 🛠️ Tecnologías Utilizadas

- Lenguaje: Kotlin
- Arquitectura: MVVM (Model - View - ViewModel)
- Base de Datos Local: Room Database
- Backend: Supabase
- Networking: Retrofit y OkHttp
- Notificaciones: Android Notification Manager
- UI: Material Design Components
- Control de versiones: Git y GitHub

---

## 📂 Estructura del Proyecto

El proyecto sigue una organización basada en **Clean Architecture**, separando la lógica de datos, la lógica de negocio y la interfaz de usuario.

```
app/src/main/java/com/example/gestionhabitos/

data/
│
├── model/
│   ├── entities/
│   ├── dao/
│
├── api/
│
├── repository/
│
├── viewmodel/
│
└── view/
    ├── fragments/
    └── adapters/
```

Descripción de carpetas:

- **data/** → Gestión del acceso a datos
- **model/entities/** → Entidades del sistema
- **dao/** → Interfaces de acceso a la base de datos
- **api/** → Servicios de conexión con Supabase
- **repository/** → Implementación del patrón Repository
- **viewmodel/** → Gestión del estado de la interfaz
- **view/** → Interfaz gráfica de la aplicación

---

## 🚀 Instalación y Uso

### 1. Clonar el repositorio

```bash
git clone https://github.com/Jeremias-Cueva/HabitFlow.git
```

### 2. Abrir el proyecto

Abrir el proyecto en **Android Studio Jellyfish** o superior.

### 3. Configurar credenciales

Configurar las siguientes credenciales:

- Credenciales de **SendGrid**
- Endpoints de **Supabase** en el cliente Retrofit

### 4. Ejecutar la aplicación

Sincronizar el proyecto con **Gradle** y ejecutar en un dispositivo o emulador con:

- **Android API 24 o superior**

---

## 📌 Estado del Proyecto

Proyecto desarrollado como parte de un trabajo académico enfocado en el desarrollo de aplicaciones móviles, utilizando arquitectura moderna y persistencia híbrida para garantizar el funcionamiento offline y la sincronización con servicios en la nube.

---

## 👨‍💻 Autores

**Henry Simbaña**

**Jeremías Cueva**
