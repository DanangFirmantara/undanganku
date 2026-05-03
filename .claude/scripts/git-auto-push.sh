#!/bin/bash
# Auto-commit and push changes to development branch after Claude stops

REPO_DIR="$(git -C "$(dirname "$0")/../.." rev-parse --show-toplevel 2>/dev/null)"

if [ -z "$REPO_DIR" ]; then
  echo '{"systemMessage": "Git auto-push: No git repository found, skipping."}'
  exit 0
fi

cd "$REPO_DIR" || exit 0

# Check if there are any changes to commit
if git diff --quiet && git diff --cached --quiet && [ -z "$(git ls-files --others --exclude-standard)" ]; then
  exit 0
fi

# Ensure we're on the development branch
CURRENT_BRANCH=$(git branch --show-current 2>/dev/null)
if [ "$CURRENT_BRANCH" != "development" ]; then
  git checkout development 2>/dev/null || git checkout -b development 2>/dev/null
fi

# Stage all changes
git add -A

# Create commit with timestamp
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
CHANGED_FILES=$(git diff --cached --name-only | head -5 | tr '\n' ', ' | sed 's/,$//')
COMMIT_MSG="chore: auto-save [${TIMESTAMP}]

Changed: ${CHANGED_FILES}"

git commit -m "$COMMIT_MSG" 2>/dev/null

# Push to origin development
if git remote get-url origin &>/dev/null; then
  git push origin development 2>/dev/null
  echo "{\"systemMessage\": \"Git: Pushed to origin/development at ${TIMESTAMP}\"}"
else
  echo '{"systemMessage": "Git: Committed locally (no remote configured)."}'
fi
