# 🧵 Multithreaded REST API Scraper

A multithreaded Java application that polls multiple public REST APIs, parses their JSON responses, and saves the data into a CSV or JSON file — depending on user preference.  
Built using **`java.util.concurrent`**, **Jackson**, and **Java HTTP Client**.

---

## 📜 Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Project Structure](#project-structure)
4. [How It Works](#how-it-works)
5. [Setup and Run](#setup-and-run)
6. [Tests](#tests)
7. [Used Technologies](#used-technologies)
8. [Original Assignment (RU)](#original-assignment-ru)

---

## 🧩 Overview
The project is a **multithreaded REST API Scraper**, designed to periodically fetch data from multiple REST APIs concurrently.  
Each API source is handled by its own thread, but the total number of active threads never exceeds a user-defined limit.  

The collected data is parsed and written into a single file (`data.json` or `data.csv`) using a shared blocking queue and writer thread.

---

## ✨ Features
- Parallel API polling with thread pool management  
- Configurable delay between requests  
- Safe concurrent writes to one shared file  
- Two output formats: JSON or CSV  
- Graceful thread interruption and recovery  
- Unit tests for parsing and data handling logic  

---

## 🧱 Project Structure
```
src/
├── core/
│   ├── APIPoller.java         # Handles API polling and data fetching
│   ├── DataWriter.java        # Writes parsed data to file
│   ├── DataRecord.java        # Unified data model for all sources
│   ├── FileFormat.java        # Enum for JSON/CSV formats
│   └── Source.java            # Enum for different API sources
│
├── exceptions/
│   ├── InvalidFileFormat.java # Custom exception for cheking format
│   └── DataWriter.java        # Custom exception for cheking source
│
├── parsers/
│   ├── CatsFactParser.java    # Parses cat fact API responses
│   ├── JokesParser.java       # Parses jokes API responses
│   └── DnDParser.java         # Parses D&D quotes API responses
│
├── utils/
│   ├── APIParser.java         # Interface of parsers
│   ├── ParserFactory.java     # Chooses correct parser based on source
│   ├── RandomRequestBuilder.java # Builds randomized request URLs
│   └── SourceChecker.java     # Checks Source in String sourceName 
│
└── tests/
    ├── core/
    │   └── DataWriterTest.java
    │   
    ├── parsers/
    │   ├── CatsFactParserTest.java
    │   ├── JokesParserTest.java
    │   └── DnDParserTest.java
    │
    └── utils/
        ├── ParserFactory.java
        └── SourceChecker.java 
````

---

## ⚙️ How It Works
1. **Startup arguments** define:
   - `n` — max number of active threads  
   - `t` — delay in seconds between API polls  
   - list of API sources  
   - output file format  

2. **APIPoller** runs per source.  
   It sends HTTP requests using `HttpClient`, parses JSON via the appropriate parser, and sends the parsed `DataRecord` into a shared `BlockingQueue`.

3. **DataWriter** continuously reads from the queue and writes data into a file.  
   When interrupted, it gracefully closes the JSON array or CSV stream.

4. **ExecutorService** ensures that no more than `n` threads are active simultaneously, while `ScheduledExecutorService` re-schedules polling tasks every `t` seconds.

---

## 🚀 Setup and Run

### 1. Prerequisites
- Java 17+
- Maven 3.8+

### 2. Clone the repository
```bash
git clone https://github.com/yourusername/multithreaded-api-scraper.git
cd multithreaded-api-scraper
````

### 3. Build the project

```bash
mvn clean package
```

### 4. Run the program

```bash
java -jar target/api-scraper.jar 3 10 CATS,JOKES,DND JSON
```

Example:
→ 3 10 DnD jokes cats JSON

---

## 🧪 Tests

The project includes unit tests for:

* JSON parsing (`CatsFactParserTest`, `JokesParserTest`, etc.)
* Factory logic (`ParserFactoryTest`)
* File writing (`DataWriterTest`)

Run tests via Maven:

```bash
mvn test
```

Target coverage: **≥ 70%**, as required by the assignment.

---

## 🧰 Used Technologies

* **Java 24**
* **Maven**
* **Jackson Databind** – JSON parsing
* **java.net.http.HttpClient** – API communication
* **java.util.concurrent** – thread management
* **JUnit 5** – unit testing framework

---

## 🗒️ Original Assignment (RU)

> **Индивидуальное задание на курсовое проектирование**
> 
> Тема: «Разработка многопоточного REST API Scrapper»
>
> Необходимо выбрать не менее трёх открытых, периодически обновляемых REST API. Разработать приложение, которое в нескольких потоках опрашивает API источников данных и сохраняет новые записи в файл формата CSV или JSON.
>
> **Требования:**
>
> * Аргументы при запуске: количество потоков, таймаут, список сервисов, формат файла
> * На каждый источник данных создаётся отдельный поток
> * Одновременно может работать не более `n` потоков
> * Все потоки пишут данные в один файл
> * Использовать `java.util.concurrent`
> * Покрытие кода тестами ≥ 70%
> * Сборка проекта через Maven или Gradle

---
xoxo maru