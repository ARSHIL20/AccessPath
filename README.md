# AccessPath

**Accessibility feedback on public places.** AccessPath helps people find out how accessible a place really is, before they go. Browse places with multi-tag filters, check at-a-glance score badges, read community reviews, and get AI-generated summaries of what reviewers are saying.

> Built as an Android app (Kotlin + Jetpack Compose) in Google AI Studio.
> View in AI Studio: https://ai.studio/apps/69e23b98-9460-405d-be6a-a06479592c23

---

## Features

- **Multi-tag filters**: narrow places by the accessibility features that matter to you (for example step-free entry, accessible restrooms, quiet space, braille signage).
- **Score badges**: quick visual accessibility scores on every place.
- **Community reviews**: first-hand accessibility feedback from other users.
- **AI feedback summaries**: Gemini condenses many reviews into a short, readable summary of strengths and barriers.

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose |
| Build | Gradle (Kotlin DSL) with a version catalog |
| Code generation | KSP |
| Secrets | Secrets Gradle Plugin (reads `.env`) |
| Backend services | Google Services plugin (Firebase) |
| AI | Gemini API (server-side) |

## Project Structure

```
AccessPath/
├── app/                   # Android application module
├── gradle/                # Gradle wrapper and version catalog (libs.versions.toml)
├── build.gradle.kts       # Top-level build file
├── settings.gradle.kts    # Project settings (includes :app)
├── gradle.properties      # Gradle configuration
├── metadata.json          # AI Studio app metadata
├── .env.example           # Template for local environment variables
└── README.md
```

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable recommended)
- A JDK supported by your Android Studio version (the project uses the Foojay toolchain resolver, so Gradle can fetch a matching JDK automatically)
- An Android emulator or a physical device
- A [Gemini API key](https://aistudio.google.com/app/apikey)

### Run Locally

1. **Open the project.** In Android Studio, choose **Open** and select the project directory.
2. **Sync Gradle.** Let Android Studio import the project and fix any incompatibilities it reports.
3. **Add your API key.** Copy `.env.example` to `.env` in the project root and set your key:

   ```properties
   GEMINI_API_KEY=your_real_gemini_api_key
   ```

   Make sure the `GEMINI_API_KEY` line is **uncommented**. If it stays commented out, the key will not be packaged into the APK and AI features will not work.
4. **Update the signing config.** In the app module's `build.gradle.kts`, remove this line:

   ```kotlin
   signingConfig = signingConfigs.getByName("debugConfig")
   ```
5. **Run.** Select an emulator or device and press **Run** (▶).

### Publishing Notes

If you already published this app from AI Studio, you will need to [request an upload key reset](https://support.google.com/googleplay/android-developer/answer/9842756#zippy=%2Crequest-an-upload-key-reset) in the Google Play Console before uploading builds signed with your own key.

## Configuration

| Variable | Required | Description |
| --- | --- | --- |
| `GEMINI_API_KEY` | Yes (for AI summaries) | Gemini API key used to generate review summaries. Set in `.env` locally; AI Studio injects it from your secrets at runtime. |

> **Security:** never commit your `.env` file. It is listed in `.gitignore`; double-check before pushing.

## Contributing

1. Fork the repository and create a feature branch.
2. Make your changes and test on an emulator or device.
3. Open a pull request describing what changed and why.

Accessibility improvements to the app itself (TalkBack labels, contrast, touch target size, dynamic text) are especially welcome.

## License

Add your license here (for example MIT or Apache-2.0).
