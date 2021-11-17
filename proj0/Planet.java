public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double GRAVITATIONAL = 6.67e-11;

    /* Constructor that takes all values to be initialized */
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }
    /* Constructor that takes another planet and initializes an identical one */
    public Planet(Planet p) {
        this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }
    /* Takes in another planet and calculates distance as change in x ^2
    plus change in y ^ 2 */
    public double calcDistance(Planet p) {
        double squaredDistance = java.lang.Math.pow((this.xxPos - p.xxPos), 2) + java.lang.Math.pow((this.yyPos - p.yyPos), 2);
        double distance = java.lang.Math.sqrt(squaredDistance);
        return distance;
    }
    public double calcForceExertedBy(Planet p) {
        return GRAVITATIONAL * this.mass * p.mass / java.lang.Math.pow(calcDistance(p), 2);
    }
    public double calcForceExertedByX(Planet p) {
        return calcForceExertedBy(p) * (p.xxPos - this.xxPos) / calcDistance(p);
    }
    public double calcForceExertedByY(Planet p) {
        return calcForceExertedBy(p) * (p.yyPos - this.yyPos) / calcDistance(p);
    }
    public double calcNetForceExertedByX(Planet[] planets) {
        double netForce = 0;
        for(Planet planet: planets) {
            if (!this.equals(planet)) {
                netForce += calcForceExertedByX(planet);
            }
        }
        return netForce;
    }
    public double calcNetForceExertedByY(Planet[] planets) {
        double netForce = 0;
        for(Planet planet: planets) {
            if (!this.equals(planet)) {
                netForce += calcForceExertedByY(planet);
            }
        }
        return netForce;
    }
    public void update(double dt, double fX, double fY) {
        double accX = fX / this.mass;
        double accY = fY / this.mass;
        this.xxVel = this.xxVel + dt * accX;
        this.yyVel = this.yyVel + dt * accY;
        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;
    }
    public void draw() {
        String image = "images/" + this.imgFileName;
        StdDraw.picture(this.xxPos, this.yyPos, image);
    }
}