# JobHub — Android Job Board & Career Tracker

A modern, high-polish native Android Job Board and Career Application Pipeline tracker built with **Kotlin** and **Jetpack Compose**.

## 📱 Features

- **Curated Job Exploration**:
  - Live feed of verified tech opportunities spanning Android, iOS, Fullstack, AI & Machine Learning, UI/UX Design, Cloud & DevOps, Product Management, and Data Science.
  - Featured & promoted roles carousel with quick badges.
  - Instant fuzzy search by role title, required skills, company, and location.
- **Dynamic Search & Multi-Criteria Filtering**:
  - Filter by Category (Engineering, Mobile, AI/ML, Design, Backend, etc.).
  - Filter by Work Arrangement (Remote, Hybrid, On-site).
  - Filter by Experience Level (Entry Level, Mid-Level, Senior, Lead/Principal).
  - Target Minimum Salary slider ($80k to $220k+).
- **Interactive Job Details**:
  - In-depth specifications: salary breakdown, role overview, daily responsibilities, qualifications, tech stack chips, company profile, and benefits/perks.
  - Direct sharing and bookmarking.
- **1-Tap Quick Apply**:
  - Interactive application modal pre-filled from user profile (name, email, phone, portfolio/LinkedIn, and cover note).
  - Auto-updates the job's applicants count and immediately registers into the Application Pipeline.
- **Application Pipeline Tracker**:
  - Kanban-style status tracking: **Applied**, **In Review**, **Interviewing**, **Offer Received**, and **Archived**.
  - Pipeline metrics summary cards (Total submitted, active reviews, upcoming interviews, offers).
  - Private notes editor and interview date scheduler for every applied position.
- **Saved / Bookmarked Jobs**:
  - Bookmark roles for quick review and one-tap application.
- **Post a Job (Hiring / Recruiter Flow)**:
  - Form allowing employers to publish new vacancies to the board with custom roles, salary ranges, skills tags, and requirements.
  - Includes an Auto-fill test shortcut for rapid demonstration.
- **Candidate Profile & Target Compensation Matcher**:
  - Professional summary, verified core skills cloud, contact information, and portfolio links.
  - Interactive target salary calculator calculating real-time matching jobs in the current market.

## 🛠 Tech Stack & Architecture

- **Language**: 100% Kotlin
- **UI Framework**: Jetpack Compose (Material Design 3 with dynamic tokens and dark/light mode)
- **Local Persistence**: Android Room Database with reactive Kotlin `Flow` & Coroutines
- **Architecture**: Clean MVVM (Model-View-ViewModel) with unidirectional data flow
- **Design & Polish**: Material 3 edge-to-edge support with `WindowInsets`, custom adaptive launcher icon, and high-fidelity visual assets

## 🚀 How to Run

1. Open project in **Android Studio** (Ladybug / Iguana or newer).
2. Sync Gradle with project files (JDK 17 or 21).
3. Select an Android emulator (API 26+) or connected physical device.
4. Click **Run** (`Shift + F10`) to build and launch `JobHub`.
