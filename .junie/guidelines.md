# Project Guidelines

This document outlines the guidelines for the OpenSea marine navigation app project.

## Project Structure
* The project is a Kotlin Multiplatform Mobile (KMM) application for marine navigation
* Source code is organized in the `src` directory
* Prompts and task descriptions are stored in the `prompts` directory

## Coding Guidelines
* Follow Kotlin coding conventions
* Use MVVM architecture pattern
* Implement dependency injection using Koin
* Write unit tests for core functionality

## Testing Requirements
* Run unit tests for any modified components
* Ensure all tests pass before submitting changes

## Documentation
* Document all public APIs
* Keep code comments up-to-date with implementation

## Prompt Management (MANDATORY)
* For every user prompt, create a file inside the prompts folder
* In this file, include not only the original prompt but also a summary of the task result
* Use a consistent naming convention for prompt files:
  * Numerical prefix to indicate sequence (e.g., "1-", "2-")
  * "H" prefix for human-originated prompts (e.g., "0H-")
  * Descriptive name that summarizes the content
