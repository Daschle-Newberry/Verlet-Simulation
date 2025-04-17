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

<h3> Grid Based Collision Algorithm </h3>

While the Verlet integration is fast to calculate, particle collisions are not. In fact, the naive (brute force) approach to particle collisions is an O(n^2) algorithm, which is incredibly slow. The naive approach can simulate 900 particles at ~60fps on a single thread on my 7800x3d. There is a fairly simple (in theory) way to scale down the amount of particle to particle collisions though. If we create a 2D array, acting as a grid, we can break particles down into cells. These cells are then used to check particles against particles who are within their spatial region. This algorithm still has a worst-case time complexity of O(n^2), but has a best case scenario of O(n). Due to collisions, a single particle should only ever be compared to 18 other particles (assuming 2 particles per cell), which is MUCH faster than comparing every particle to every other particle. In testing, I got the following performance results:

| Method | Particles | Collision Checks per Simulation Frame | FPS  |
|--------|-----------|----------------------------------------|------|
| Grid  | 6,000     | 48,147                                 | ~140 |
| Naive   | 6,000     | 35,994,000                             | ~1   |
| Grid  | 10,000     | 87,374                                 | ~75 |
| Naive   | 10,000     | 99,990,000                              | ~.3  |

As you can see, the grid based approach results in a massive performance increase over the naive approach.

<h3> GPU Instancing </h3>
Drawing the scene to the screen also introduces a bottleneck when simulating thousands of particles. Whenever a particle is drawn, the CPU has to issue a costly operation called a draw call, which sends the key information to the GPU on what to draw. Draw calls usually aren't a performance issue if you are only drawing a thousand to a few thousands objects (depending on the system). In the case of this simulation though, we are drawing upwards of 10,000 objects to the screen. The performace hit from these draw calls is large, especially since the CPU is already doing thousands of calculations per frame for the simulation. To get around this, we use something call GPU instancing, which utilizes the same mesh data to draw objects. We first send in the mesh data using a buffer, which then lives on the GPU for the life of the application. Then, we send a second buffer per frame called the instance buffer. This buffer contain the particle positon (in matrix form) and particle color. This data is then parsed on the GPU and used to draw each particle. This method takes the total draw calls from O(n) to O(1), which can result in a sizeable performance increase:


| Method       | Particles    | Render Time (ns)   | FPS  |
|--------------|--------------|---------------|------|
| Regular      | 6,000  | 8,236            | ~115 |  
| Instancing   | 6,000  | 123         | ~140   | 
| Regular      | 10,000 | 13,274            | ~70 |
| Instancing   | 10,000 | 134        | ~95   |   

With GPU instancing we see about a 98% - 99% decrease in render time. While this seems like a lot, we were only spending .008ms drawing the scene with regular draw calls, so it doesn't have as massive of a performace increase as the grid optimization. The result however does see about a 26% increase in FPS, which not insignificant. 





