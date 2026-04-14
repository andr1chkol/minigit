# MiniGit

MiniGit is a small Git-inspired command-line tool written in Java.

It implements a simplified version control workflow with repository initialization, staging, commits, logs, status checks, and checkout by commit id.

## Features

- Initialize a MiniGit repository
- Add files to the index
- Store file contents as hashed blobs
- Create commits with messages and parent references
- Show commit history
- Show staged, modified, and untracked files
- Checkout files from a specific commit

## Tech Stack

- Java
- Gradle
- SHA-1 hashing
- File-based object storage

## Project Structure

```text
src/main/java/minigit
├── cli        # Application entry point
├── commands   # CLI command implementations
├── core       # Command dispatching and repository path helpers
├── domain     # Domain models
├── storage    # Index, object, and commit storage
└── util       # Utility classes
```

## Usage

Build the project:

```bash
./gradlew build
```

Run a command:

```bash
java -cp build/classes/java/main minigit.cli.Main <command>
```

## Commands

### init

Initializes a new MiniGit repository in the current directory.

```bash
java -cp build/classes/java/main minigit.cli.Main init
```

Creates the following structure:

```text
.minigit/
├── HEAD
├── index
├── objects/
│   ├── blobs/
│   └── commits/
└── refs/
    └── heads/
        └── main
```

### add

Adds a file to the index and stores its content as a blob.

```bash
java -cp build/classes/java/main minigit.cli.Main add file.txt
```

### commit

Creates a commit from the current index.

```bash
java -cp build/classes/java/main minigit.cli.Main commit "initial commit"
```

### log

Shows the commit history starting from the current `main` reference.

```bash
java -cp build/classes/java/main minigit.cli.Main log
```

### status

Shows staged, modified, and untracked files.

```bash
java -cp build/classes/java/main minigit.cli.Main status
```

### checkout

Restores files from a specific commit.

```bash
java -cp build/classes/java/main minigit.cli.Main checkout <commit-id>
```

## Example

```bash
java -cp build/classes/java/main minigit.cli.Main init
echo "hello" > hello.txt
java -cp build/classes/java/main minigit.cli.Main add hello.txt
java -cp build/classes/java/main minigit.cli.Main commit "add hello file"
java -cp build/classes/java/main minigit.cli.Main log
```

## How It Works

MiniGit stores file contents as blobs using SHA-1 hashes. When a file is added, its content is saved in `.minigit/objects/blobs/`, and the index stores a mapping between the file path and the blob id.

When a commit is created, MiniGit stores the commit message, parent commit id, and indexed files in `.minigit/objects/commits/`. The current commit id is stored in `.minigit/refs/heads/main`.

## Limitations

This project intentionally implements only a simplified subset of Git.

Current limitations:

- No branches besides `main`
- No merge support
- No diff output
- No deletion tracking
- No remote repositories
- No conflict handling
- No `.gitignore` support
- Commit messages are currently read from the first CLI argument

## Project Goal

The goal of this project was to deepen my Java Core knowledge, apply it in practice, and better understand the structure and internal workflow of Git-like version control systems.
