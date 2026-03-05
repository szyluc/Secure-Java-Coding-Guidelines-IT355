# Secure Java Coding Guidelines - IT355

This repository contains:

- A group Java project in `project/`
- Individual secure coding rule/recommendation examples in each team member's folder

## Brief Project Description: Library Application

The group project we built is a Library Application, created for both consumer and administrative use.

The Library Application allows consumers and administrators to perform a variety of actions, such as:

- Account login / creation
- Rent a book from the library
- Return a book to the library
- See the current books rented
- Search for books by name, author, and category
- (Admin) Look up a user's account
- (Admin) Compare two users' accounts
- (Admin) Add a book to the library system
- (Admin) Remove a book from the library system

## Navigate to this project

From PowerShell:

```powershell
cd path\to\Secure-Java-Coding-Guidelines-IT355
```

Then move into the runnable app folder:

```powershell
cd project
```

## Run the Java project

The app in `project/` includes helper scripts that compile and run the code.

### Windows

```powershell
.\run.cmd
```

### macOS/Linux

```bash
chmod +x run.sh
./run.sh
```

Both scripts compile all Java files in `project/` and run `Driver` using the included SQLite JDBC jar in `project/lib/`.

## Where the rules/recommendations are

Secure coding rules and recommendations are organized by person in their individual folders at the repository root:

- `brady/`
- `jd/`
- `kory/`
- `luis/`
- `patrick/`
- `szymon/`

Each folder contains that person's rule/recommendation files and examples.
