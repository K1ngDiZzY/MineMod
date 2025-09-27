# MineMod

[![CI](https://github.com/K1ngDiZzY/MineMod/actions/workflows/ci.yml/badge.svg)](https://github.com/K1ngDiZzY/MineMod/actions/workflows/ci.yml)
[![Release](https://github.com/K1ngDiZzY/MineMod/actions/workflows/cd.yml/badge.svg)](https://github.com/K1ngDiZzY/MineMod/actions/workflows/cd.yml)
[![License](https://img.shields.io/badge/License-LGPL%202.1-blue.svg)](LICENSE.txt)
[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)
[![Forge](https://img.shields.io/badge/Forge-58.1.6-green.svg)](https://files.minecraftforge.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.8-brightgreen.svg)](https://www.minecraft.net/)

A One Piece-themed Minecraft mod that adds new features and enhancements to the game.

## 🚀 Features

- Custom blocks, items, and entities
- One Piece themed content
- Built with Minecraft Forge 58.1.6 for Minecraft 1.21.8

## 🛠 Development

### Prerequisites

- Java 21 (Temurin recommended)
- Git

### Building

```bash
git clone https://github.com/K1ngDiZzY/MineMod.git
cd MineMod
./gradlew build
```

The built mod JAR will be available in `build/libs/`.

### Local Setup Verification

Before pushing changes, you can verify your local setup:

```bash
./scripts/verify-local-setup.sh
```

This script checks:
- Java 21 installation
- Gradle wrapper configuration
- Build compilation
- Test execution
- CI/CD workflow syntax

### Development Setup

For Eclipse:
```bash
./gradlew genEclipseRuns
```

For IntelliJ IDEA:
```bash
./gradlew genIntellijRuns
```

### Running

- **Client**: `./gradlew runClient`
- **Server**: `./gradlew runServer`
- **Data Generation**: `./gradlew runData`

## 🔄 CI/CD

This project uses GitHub Actions for continuous integration and deployment:

- **CI**: Automatically builds and tests on Linux, Windows, and macOS
- **PR Validation**: Validates pull requests with build and test results
- **CD**: Automatically creates releases when version tags are pushed
- **Dependency Updates**: Dependabot keeps dependencies up to date

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the LGPL 2.1 License - see the [LICENSE.txt](LICENSE.txt) file for details.

## 👥 Authors

- **K1ngDiZzY** - *Initial work*
- **DJohnson** - *Contributor*

---

*Built with ❤️ for the Minecraft modding community*