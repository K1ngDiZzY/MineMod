# CI/CD Documentation

This document describes the continuous integration and deployment setup for the MineMod project.

## 🚀 Overview

The project uses GitHub Actions for automated building, testing, and deployment. The CI/CD pipeline consists of several workflows:

## 🔄 Workflows

### 1. CI (`ci.yml`)
**Triggers:** Push to `main`/`develop`, Pull Requests to `main`

**Features:**
- Multi-platform builds (Ubuntu, Windows, macOS)
- Java 21 with Temurin distribution
- Gradle dependency caching
- Automated testing with JUnit 5
- Artifact upload (mod JARs, test results)
- Code quality checks
- Security scanning (on main branch only)

### 2. CD - Release (`cd.yml`)
**Triggers:** Version tags (`v*.*.*`), Published releases

**Features:**
- Automated release creation
- JAR artifact publishing to GitHub Releases
- Changelog extraction from `changelog.txt`
- Pre-release detection (alpha, beta, rc versions)
- Long-term artifact retention (90 days)

### 3. PR Validation (`pr.yml`)
**Triggers:** Pull request opened/updated

**Features:**
- Build validation
- Test execution
- Automated PR comments with results
- Lint checking
- Artifact upload for review

### 4. Workflow Validation (`validate.yml`)
**Triggers:** Changes to `.github/` directory

**Features:**
- YAML syntax validation
- Common issue detection
- Configuration verification

## 🔧 Dependencies Management

### Dependabot (`dependabot.yml`)
- Weekly Gradle dependency updates (Sundays 4:00 AM)
- GitHub Actions security updates
- Auto-assigns to project maintainers
- Proper labeling for easy identification

## 📋 Requirements

### Environment
- **Java:** 21 (Temurin distribution)
- **Gradle:** 8.12.1+ (via wrapper)
- **Minecraft:** 1.21.8
- **Forge:** 58.1.6

### Permissions
The workflows require the following permissions:
- `contents: write` - For creating releases and uploading artifacts
- `actions: read` - For workflow execution
- `security-events: write` - For security scanning (optional)

## 🏗️ Build Process

1. **Checkout** - Repository code retrieval
2. **Environment Setup** - Java 21 installation and Gradle caching
3. **Dependencies** - Gradle dependency resolution
4. **Compilation** - Source code compilation
5. **Testing** - JUnit test execution
6. **Packaging** - JAR file creation
7. **Quality Checks** - Code analysis and security scanning
8. **Artifacts** - Upload build outputs

## 🧪 Testing

### Test Structure
```
src/test/java/
└── com/example/examplemod/
    └── ExampleModTest.java
```

### Test Configuration
- **Framework:** JUnit 5 (Jupiter)
- **Execution:** Gradle test task
- **Reporting:** HTML and XML reports
- **Coverage:** Future enhancement

## 🚀 Deployment

### Release Process
1. Create and push a version tag: `git tag v1.0.0 && git push origin v1.0.0`
2. CD workflow automatically triggers
3. Build and test verification
4. GitHub release creation with artifacts
5. JAR files attached to release

### Pre-releases
Versions containing `alpha`, `beta`, or `rc` are marked as pre-releases.

## 🔍 Monitoring

### Build Status
- CI status badges in README.md
- Workflow run history in Actions tab
- Email notifications on failure (configurable)

### Artifacts
- **Retention:** 30 days (build artifacts), 90 days (releases)
- **Storage:** GitHub Packages (future enhancement)
- **Distribution:** GitHub Releases

## ⚙️ Configuration

### Environment Variables
- `GRADLE_OPTS`: "-Dorg.gradle.daemon=false -Xmx5G"

### Secrets Required
- `GITHUB_TOKEN` (automatically provided)

### Cache Strategy
- Gradle dependencies: `~/.gradle/caches` and `~/.gradle/wrapper`
- Key: OS + Gradle files hash
- Restoration: Partial key matching

## 🔧 Maintenance

### Regular Tasks
- Weekly dependency updates via Dependabot
- Monthly workflow review and optimization
- Quarterly security audit

### Troubleshooting
- Check workflow logs in Actions tab
- Verify Java 21 compatibility
- Ensure Gradle wrapper permissions
- Review artifact upload paths

## 📈 Future Enhancements

- [ ] Integration with mod distribution platforms (CurseForge, Modrinth)
- [ ] Code coverage reporting with Codecov
- [ ] Integration testing with Minecraft server
- [ ] Performance benchmarking
- [ ] Documentation generation and deployment
- [ ] Discord/Slack notifications for releases