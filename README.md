# Day Counter Challenge

A game-like Android app that calculates the number of days a challenge must be completed over based on the roll of three dice.

## How to Play

1. **Roll the Dice**: Tap the "Roll Dice" button to roll three dice
2. **Dice Meanings**:
   - **First Dice**: Determines the number of days multiplied by the current multiplier
   - **Second Dice**: Determines if the challenge is complete (1 means complete) and adds to multiplier for next round
   - **Third Dice**: Adds its value to the multiplier for the next round

## Special Rules

- **Two Same Dice**: Multiplier is increased both before and after the round
- **Three Same Dice**: Second dice is set to 6, third dice is rolled twice, and the total is added to multiplier before and after
- **All Sixes**: Base days are set to 10 and multiplier is increased by 5

## Features

- **Persistent Storage**: Game state is saved locally and persists across app launches
- **Round History**: View all previous rounds and their results
- **Statistics**: Track total days, current round, and multiplier
- **Modern UI**: Built with Jetpack Compose and Material 3

## Architecture

The app follows clean architecture principles with clear separation:

- **Data Layer**: Models, Repository for local persistence
- **Domain Layer**: Game logic service
- **Presentation Layer**: ViewModels, UI Composables
- **Dependency Injection**: Simple container for managing dependencies

## Technology Stack

- **Kotlin** with Jetpack Compose
- **Material 3** design system
- **DataStore** for local persistence
- **Kotlinx Serialization** for data serialization
- **MVVM Architecture** with ViewModels
