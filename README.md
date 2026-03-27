<h1 align="center">Questua - Backend</h1>

<p align="center">
  Gamified language learning platform powered by quests, dialogues and progression systems
</p>

---

## Overview

Questua is a backend system designed to support a gamified language learning experience.

The platform models learning as an interactive journey, where users progress through cities, quests, and dialogues, with real-time tracking of performance, achievements, and progression.

---

## Tech Stack

- Java (Spring Boot)
- PostgreSQL
- JPA / Hibernate
- REST API
- JSONB (for dynamic content modeling)

---

## Architecture

The backend follows a **layered architecture**:

- **Controller Layer** → REST endpoints
- **Service Layer** → business rules and orchestration
- **Repository Layer** → database access
- **Domain Layer** → core entities

---

## Core Concepts

### Gamification System

- XP, levels and streaks
- Achievements with rarity system
- Progress tracking per language

### Quest System

- Cities → Quest Points → Quests → Dialogues
- Structured progression model
- Difficulty scaling and unlock requirements

### Dialogue Engine

- Interactive dialogues with:
  - branching logic
  - user input (text / choice)
  - progression control
- Supports future expansion with AI-generated content

### User Progress Tracking

- Quest completion percentage
- Score and performance evaluation
- Response storage and analysis

### Monetization

- Products linked to content (quests, cities, etc.)
- Transaction system with status tracking
- Ready for Stripe integration

### AI Integration (Design-Ready)

- Logging of AI-generated content
- Support for multiple generation targets:
  - quests
  - dialogues
  - characters
- Tracking of generation status and metadata

---

## Database Design

The system uses a **relational model enhanced with JSONB** for flexibility.

### Key Highlights

- UUID-based entities
- Strong relational integrity via foreign keys
- ENUMs for domain consistency
- JSONB for:
  - dynamic dialogues
  - unlock conditions
  - gamification metadata

### Main Entities

- `user_account`
- `user_language`
- `city`
- `quest_point`
- `quest`
- `scene_dialogue`
- `user_quest`
- `achievement`
- `transaction_record`
- `ai_generation_log`

---

## Business Rules (Highlights)

- Users can track multiple languages independently
- Progress is tied to quests and dialogue states
- XP and achievements are computed dynamically
- Unlockable content based on progression and requirements
- Transactions linked to specific in-game content

---

## API Design

- RESTful endpoints
- Resource-oriented structure
- Clear separation between domain entities

---

## Getting Started

### Requirements

- Java 17+
- PostgreSQL
- Maven

### Setup

1. Clone the repository
2. Configure database connection
3. Run SQL script (included)
4. Start application

---

## Project Context

This project focuses on:

- Scalable backend design
- Complex relational modeling
- Gamification systems
- Real-world product architecture patterns

---

## License

Educational project.
