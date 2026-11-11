# Git and GitHub workflow (refresher)

This project is a **fork**: your own copy of someone else's repository on
GitHub. You work and push in your fork, and you can offer changes back to the
original with a pull request.

```
smartqubit/projectlibre        <- "upstream": the original. You can't push here.
        |  (Fork button on GitHub)
        v
<you>/projectlibre             <- "origin": your fork. You push here.
        |  (git clone)
        v
your computer / codespace      <- where you edit, build, and commit
```

## One-time setup

1. **Fork** (in the browser): open https://github.com/smartqubit/projectlibre
   and click **Fork**, then **Create fork**. Keep the name `projectlibre`.
2. **Clone your fork** (replace `<you>` with your GitHub username):

   ```bash
   git clone https://github.com/<you>/projectlibre.git
   ```

   ```bash
   cd projectlibre
   ```

3. **Point at the original** so you can pull its updates later:

   ```bash
   git remote add upstream https://github.com/smartqubit/projectlibre.git
   ```

   `git remote -v` should now list `origin` (your fork) and `upstream`.

4. **Tell git who you are** (once per computer):

   ```bash
   git config --global user.name "Your Name"
   ```

   ```bash
   git config --global user.email "you@example.com"
   ```

5. **Signing in for `git push`.** GitHub no longer accepts your account
   password on the command line. The easiest fix is the GitHub CLI: install it
   from https://cli.github.com, then run `gh auth login` once. GitHub Desktop
   and VS Code's built-in GitHub sign-in also work.

## Everyday loop

Use one branch per piece of work. Keep `master` as a clean copy of upstream.

```bash
git switch master
```

```bash
git pull upstream master
```

```bash
git switch -c ui/better-timescale
```

Then edit, build (`scripts/build.sh`) and test. Save your work with:

```bash
git add -A
```

```bash
git commit -m "Gantt: lighter timescale header"
```

```bash
git push -u origin ui/better-timescale
```

After the first push, plain `git push` is enough on that branch.

## Pull requests

A pull request (PR) asks for a branch to be merged.

* **Into your own fork's `master`:** open your fork on GitHub. A yellow
  banner offers **Compare & pull request**. Make sure the *base repository* is
  `<you>/projectlibre`, not `smartqubit/projectlibre`. Use this to review your
  own work and see the CI build and screenshot before merging.
* **Back to the original project (optional):** same button, with the base
  repository set to `smartqubit/projectlibre`. Only do this once the change
  is polished. The maintainers decide whether to accept it.

Each PR runs the **Build** workflow (`.github/workflows/build.yml`). Open the
run's *Summary* page to download the built jar and a screenshot of the Gantt
view.

## Getting updates from the original project

```bash
git switch master
```

```bash
git pull upstream master
```

```bash
git push origin master
```

Then bring your feature branch up to date:

```bash
git switch ui/better-timescale
```

```bash
git merge master
```

## Handy commands

| Command | What it does |
|---|---|
| `git status` | What changed and what's staged |
| `git diff` | Exact line changes not yet staged |
| `git log --oneline -15` | Recent commits |
| `git switch <branch>` | Change branch |
| `git restore <file>` | Throw away uncommitted edits to a file (no undo!) |
| `git stash` / `git stash pop` | Park and restore uncommitted work |

## Working with Claude Code

When you start a Claude Code session on claude.ai/code, pick your fork
(`<you>/projectlibre`) as the repository. Claude reads `CLAUDE.md` for
project instructions, works on a branch, and pushes it to your fork. Ask it to
open a pull request when you want one.
