![Alt](src/main/resources/assets/gif/Simulationgif.gif "simulationgif")

This project is a Verlet integration particle simulation that is capable of simulating up to ten thousand particles at 60 FPS using purely sequential CPU compute. 

<h2> How to Build (Windows)</h2>
<strong>This project was built using JDK 23, using prior JDK versions may cause unexpected behavior</strong>
<p align = "left">
  
1. First clone the repository using the command below in the terminal

```
git clone https://github.com/Daschle-Newberry/Verlet-Simulation
```

2. Once within the project directory, run the following command. This will create an jar file for the project that contains all the necessary resources

```
.\gradlew.bat shadowjar
```

3. Locate the jar file, it should be in the build\libs folder

4. Copy the jar location and run this command in the terminal
  
```
java -jar C:\Users\...\path\to\the\jar
```

</p>

<h2> Why Verlet? </h2>

The Verlet integration is an incredibly cheap way to simulate physics based off a particles current and previous positions. You can calculate an objects new position based on the following equation:

![verletintegration](https://github.com/user-attachments/assets/afdfa095-b12f-422b-a051-7af170b18db9)

The beauty of this equation is the simplicity and stability of the particle systems it is deployed in. Since velocity is calculated by subtracting the current position from the previous position, it avoids cumbersome velocity calculations that can accumulate errors. This also means that velocity changes from collisions are handled automatically, which makes particle collisions much easier to calculate.


<h2> Optimizations </h2>

While the Verlet integration is fast to calculate, particle collisions are not. In fact, the naive (brute force) approach to particle collisions is an O(n^2) algorithm, which is incredibly slow. The naive approach can simulate 900 particles at ~60fps on a single thread on my 7800x3d. There is a fairly simple (in theory) way to scale down the amount of particle to particle collisions though. If we create a 2D array, acting as a grid, we can break particles down into cells. These cells are then used to check particles against particles who are within their spatial region. This algorithm still has a worst-case time complexity of O(n^2), but has a best case scenario of O(n). Due to collisions, a single particle should only ever be compared to 18 other particles (assuming 2 particles per cell), which is MUCH faster than comparing every particle to every other particle. In testing, I got the following performance results:

|     | Particles  |Collision Checks per Simulation Frame| FPS|
|-----|------|------|------|------|
| Naive |  6,000 |  48,147 | ~140 |
| Grid  | 6,000  | 35,994,000| ~1|
