public class NBody {
    public static void main(String[] args) {
        double T = Double.valueOf(args[0]);
        double dt = Double.valueOf(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        String imageToDraw = "images/starfield.jpg";
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        double time = 0;
        while(time <= T) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for(int index = 0; index < planets.length; index++) {
                xForces[index] = planets[index].calcNetForceExertedByX(planets);
                yForces[index] = planets[index].calcNetForceExertedByY(planets);
            }
            for(int index = 0; index < planets.length; index++) {
                planets[index].update(dt, xForces[index], yForces[index]);
            }
            StdDraw.picture(0, 0, imageToDraw);
            for(Planet planet: planets) {
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                            planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                            planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
        
    }
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int numberOfPlanets = in.readInt();
        double radius = in.readDouble();
        return radius;
    }
    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int numberOfPlanets = in.readInt();
        Planet[] planets = new Planet[numberOfPlanets];
        double radius = in.readDouble();
        int index = 0;
        while(index < planets.length) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String file = in.readString();
            planets[index] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, file);
            index++;
        }
        return planets;
    }
}