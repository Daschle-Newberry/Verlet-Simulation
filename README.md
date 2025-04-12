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
