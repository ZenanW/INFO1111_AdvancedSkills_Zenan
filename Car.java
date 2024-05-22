public class Car {

    int speed;
    String sound;
    
    public Car(){

        this.speed = 60;
        this.sound = "Vroom!";

    }

    public void main(String[] args){

        System.out.println("This car is going at " + this.speed + " km/h " + this.sound);

    }
}
