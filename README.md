# Лабораторная работа №1: Вариант 3: Лабиринт с динамическими препятствиями

Данный проект представляет собой выполненную первую лабораторную работу по дисциплине **Автоматное программирование**.

Автор: **Миронов Р.С. группа 23ИП1б**

## Описание проекта

В проекте реализована задача об лабиринте с динамическими препятствиями, где конечный автомат, управляющий агентом, оптимизируется с помощью островного генетического алгоритма и стандартного генетического алгоритма. Основные особенности проекта:

- **Симуляция работы автомата:**

  Агент перемещается по игровому полю размером 32×32 клетки, с лабиринтом и динамическими препятствиями. Агент действует согласно набору правил (генотипу), определяющему его поведение.
- **Оптимизация автомата:**

  Для поиска оптимального автомата применяется островной генетический алгоритм и стандартный генетический алгоритм. Популяция делится на несколько островов, которые эволюционируют независимо, с периодической миграцией лучших особей между островами.
- **Графический интерфейс:**

  Реализован с использованием шаблонизатора `thymeleaf`. Интерфейс отображает игровое поле, динамику движения агента и препятствий, количество шагов и результата прохождения.
- **Настройка параметров:**

  Основные параметры симуляции и генетического алгоритма (например, максимальное число шагов, размер популяции, количество поколений, вероятность мутации и кроссовера) можно изменять непосредственно через интерфейс.
- **Перезапуск эволюции:**

  После завершения процесса эволюции предусмотрена возможность его перезапуска для поиска нового оптимального решения.

## Условие задачи

**Задача об агенте в лабиринте:**

- **Игровое поле:**

  Поле размером 32×32 клетки.
- **Лабиринт**

  На поле размещается фиксированный набор стен преграждающих путь к выходу.
- **Динамические препятствия**

На поле случайным образом передвигаются препятствия при столкновении с которыми игра завершается.
- **Агент:**

  Агент стартует из левого верхнего угла, его поле зрения ограничено (он видит только клетку непосредственно перед собой).
- **Действия агента:**
    - Движение вперёд;
    - Поворот налево;
    - Поворот направо;
    - Остановка.
- **Цель:**

  За 200 шагов агент должен достичь выхода в правом нижнем углу.

**Задача лабораторной работы:**

Разработать конечный автомат для управления агентом и оптимизировать его параметры с помощью генетических алгоритмов. Необходимо реализовать визуальную симуляцию, позволяющую наблюдать за ходом выполнения алгоритма, и динамическим обновлением игрового поля. Также требуется обеспечить возможность настройки параметров симуляции и перезапуска эволюции через графический интерфейс.

## Структура проекта

```
com.qwerty.mazeagentgame/           # Основной пакет проекта
├── controller/                     # Контроллеры для обработки запросов        
│   └── MazeController.java        
├── evolution/                      # Пакет с реализацией эволюционных алгоритмамов
│   ├── GeneticAlgorithm.java       
│   ├── Individual.java             
│   ├── Island.java                 
│   ├── IslandGA.java               
│   └── SimulatedAnnealing.java         
├── model/                         # Модели
│   ├── Gene.java            
│   ├── Maze.java         
│   └── Position.java          
├── service/                       # Сервисы для бизнес-логики
│   └── MazeService.java               
├── simulation/                    # Пакет с реализацией симуляции поведения агента
│   └── AgentSimulator.java   
├── util/                          # Утилитарные классы и константы
│   ├── Action.java             
│   ├── Constants.java                     
│   └── Orientation.java 
├── Main.java                      # Главный класс приложения 
├── style.css                      # css стили для интерфейса
├── index.html                     # Реализация графического интерфейса
├── application.yml                # Конфигурационный файл приложения
├── pom.xml                        # Файл конфигурации Maven 
└── README.md                      # Документация проекта       
```

## Запуск проекта

### Требования

- Java 17+
- SpringBoot 3.4.3+
- Lombok 1.18.30+

### Инструкция по запуску

1. Склонируйте репозиторий:

   ```bash
   git clone https://github.com/ekwqOdnjw9qjr/MazeWithDynamicObstacles.git
   cd MazeWithDynamicObstacles
   ```
2. Убедитесь, что у вас установлен JDK 17+. и необходимые библиотеки.
3. Запустите программу:

   ```bash
   mvn clean install
   java -cp target/Maze-Agent-game.jar com.qwerty.mazeagentgame.Main

   ```
4. Перейдите по адресу http://localhost:8087/
5. В появившемся окне:

    - Используйте поля настроек для изменения параметров симуляции и алгоритма.
    - Нажмите кнопку **"Применить настройки"** для сохранения изменений.
    - Нажмите **"Эволюция"** для запуска процесса эволюции.
    - После завершения эволюции можно запустить симуляцию лучшего автомата кнопкой **"Запустить лучшего"** чтобы узнать результат прохождения сразу, если хотите увидеть каждый шаг по отдельности, то нажмите на кнопку **"Шаг за шагом"** или перезапустить эволюцию кнопкой **"Перезапустить эволюцию"**.

## Пример использования

При запуске эволюции в информационной панели будет отображаться лучший результат эволюции статус прохождения лабиринта и количество потраченных шагов. Во время симуляции на игровом поле наблюдается перемещение агента и динамических объектов, а также происходит обновление информации о текущем шаге.

---
