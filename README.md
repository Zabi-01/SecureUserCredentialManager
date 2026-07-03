# SecurePass Manager

SecurePass Manager is a desktop-based credential management system built in Java using the Swing UI library. It provides a secure, offline environment for managing user credentials, implementing local storage encryption, Multi-Factor Authentication (MFA), and a dual-role dashboard system (User and Administrator).

## Features

* **User Authentication**: Secure Sign-up and Login interfaces.
* **Multi-Factor Authentication (MFA)**: Adds an extra layer of security with a 6-digit MFA verification code required for both user and admin access.
* **User Dashboard**:
  * **Add Credentials**: Store website details, usernames, passwords, and custom notes.
  * **View & Manage Vault**: Search, view, and organize saved credentials.
* **Admin Dashboard**:
  * Access with administrator credentials.
  * View list of all registered users (showing username, email, and count of saved credentials).
  * Track activity using real-time system logs.
  * View individual user vaults (displays encoded credential strings).
* **Data Security**: Encrypts and decodes vault data using Base64 encoding.

## Application Showcase

![SecurePass Manager UI Showcase](screenshots/gui_showcase.png)

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or higher installed.

### Compilation and Execution

1. Clone this repository (if not done already):
   ```bash
   git clone https://github.com/Zabi-01/SecureUserCredentialManager.git
   ```
2. Navigate to the project directory:
   ```bash
   cd SecureUserCredentialManager
   ```
3. Compile the Java files:
   ```bash
   javac *.java
   ```
4. Run the application:
   ```bash
   java SecurePassManager
   ```

## Project Structure

* `users/`: Local folder containing user profile configurations (`profile.dat`) and secure password vaults (`vault.dat`) (git-ignored for security).
* `logs/`: Contains system event logs (`system.log`) (git-ignored).
* `screenshots/`: Project interface preview assets.
